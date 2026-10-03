package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.message.data.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.channel.ChannelValidator;
import com.sprint.mission.discodeit.service.user.UserValidator;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final ChannelValidator channelValidator;
    private final UserValidator userValidator;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;


    @Override
    @Transactional
    public MessageDto save(
            MessageCreateRequestDto requestDto,
            List<BinaryContentCreateRequestDto> messageContentCreateRequests
    ) {
        User user = userValidator.getOrThrow(requestDto.authorId());
        Channel channel = channelValidator.getOrThrow(requestDto.channelId());

        Message savedMessage = Message.create(requestDto.content(), user, channel);

        List<BinaryContent> contents = Optional.ofNullable(messageContentCreateRequests)
                .filter(list -> !list.isEmpty())
                .map(messageBinaryContents ->
                        messageBinaryContents.stream().map(messageBinaryContent -> {
                            BinaryContent binaryContent = messageBinaryContent.toEntity();
                            BinaryContent savedContent = binaryContentRepository.save(binaryContent);
                            binaryContentStorage.put(savedContent.getId(), messageBinaryContent.bytes());
                            log.info("메세지 파일 저장");
                            return savedContent;
                        }).toList()
                )
                .orElse(Collections.emptyList());

        savedMessage.addAttachments(contents);
        messageRepository.save(savedMessage);

        log.info("메세지 저장 성공");
        return messageMapper.toDto(savedMessage);
    }

    @Override
    public MessageDto find(MessageIdRequestDto requestDto) {

        Message message = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND));

        return messageMapper.toDto(message);

    }


    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findAllByChannelId(
            ChannelIdRequestDto requestDto,
            Pageable pageable,
            String cursor
    ) {
        channelValidator.getOrThrow(requestDto.getId());
        Slice<Message> messagesSlice;

        if (cursor == null || cursor.isBlank()) {
            messagesSlice = messageRepository.findFirstPage(requestDto.getId(), pageable);
        } else {
            messagesSlice = messageRepository.findByChannelId(requestDto.getId(), Instant.parse(cursor), pageable);
        }
        String nextCursor = null;
        if (messagesSlice.hasNext() && messagesSlice.hasContent()) {
            List<Message> contents = messagesSlice.getContent();
            nextCursor = contents.get(contents.size() - 1).getCreatedAt().toString();
        }
        Slice<MessageDto> dtoSlice = messagesSlice.map(messageMapper::toDto);

        return pageResponseMapper.fromSlice(dtoSlice, nextCursor);
    }

    @Override
    @Transactional
    public MessageDto update(
            MessageIdRequestDto messageIdRequest,
            MessageUpdateRequestDto request
    ) {
        Message updateMessage = messageRepository.findById(messageIdRequest.getId())
                .orElseThrow(() -> {
                    log.warn("업데이트할 메세지가 존재하지 않음 id :{}", messageIdRequest.getId());
                    return new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND);
                });
        updateMessage.update(request.getNewContent());

        log.info("메세지 수정완료");
        return messageMapper.toDto(updateMessage);
    }


    @Override
    @Transactional
    public void delete(MessageIdRequestDto requestDto) {
        Message deleteMessage = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> {
                    log.warn("삭제할 메세지가 존재하지 않음 id :{}", requestDto.getId());
                    return new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND);
                });
        List<BinaryContent> deletedBinaryContent = deleteMessage.getAttachments().stream().toList();
        binaryContentRepository.deleteAll(deletedBinaryContent); // 수정 파일 Id 값들 전부 데이터 삭제
        deletedBinaryContent.stream().map(BaseEntity::getId)
                .forEach(binaryContentStorage::delete);
        messageRepository.delete(deleteMessage); // 메시지 삭제
        log.info("메세지 삭제완료");
    }
}
