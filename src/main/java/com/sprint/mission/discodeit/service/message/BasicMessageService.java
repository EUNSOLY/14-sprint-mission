package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.controller.common.PageResponse;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.message.data.MessageDto;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.channel.ChannelValidator;
import com.sprint.mission.discodeit.service.user.UserValidator;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final ChannelValidator channelValidator;
    private final UserValidator userValidator;
    private final UserMapper userMapper;


    @Override
    public MessageDto save(
            MessageCreateRequestDto requestDto,
            List<BinaryContentCreateRequestDto> messageContentCreateRequests
    ) {

        User user = userValidator.getOrThrow(requestDto.authorId());
        Channel channel = channelValidator.getOrThrow(requestDto.channelId());

        Message savedMessage = Message.create(requestDto.content(), user, channel);

        List<BinaryContent> contents = Optional.ofNullable(messageContentCreateRequests)
                .filter(list -> !list.isEmpty())
                .map(messageBinaryContents -> {
                    return messageBinaryContents.stream().map(messageBinaryContent -> {
                        BinaryContent binaryContent = messageBinaryContent.toEntity();
                        BinaryContent savedContent = binaryContentRepository.save(binaryContent);
                        binaryContentStorage.put(savedContent.getId(), messageBinaryContent.bytes());
                        return savedContent;
                    }).toList();
                })
                .orElse(Collections.emptyList());

        savedMessage.addAttachments(contents);
        messageRepository.save(savedMessage);

        return this.toMessageDto(savedMessage);
    }

    @Override
    public MessageDto find(MessageIdRequestDto requestDto) {

        Message message = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND));

        return this.toMessageDto(message);

    }


    @Override
    public PageResponse<List<MessageDto>> findAllByChannelId(ChannelIdRequestDto requestDto, Pageable pageable) {

        channelValidator.getOrThrow(requestDto.getId());

        Page<Message> messagesPage = messageRepository.findByChannelId(requestDto.getId(), pageable);
        List<MessageDto> message = messagesPage.getContent().stream()
                .map(this::toMessageDto).toList();

        return PageResponse.to(
                message,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                messagesPage.hasNext(),
                messagesPage.getTotalElements()
        );
    }

    @Override
    @Transactional
    public MessageDto update(
            MessageIdRequestDto messageIdRequest,
            MessageUpdateRequestDto request
    ) {
        Message updateMessage = messageRepository.findById(messageIdRequest.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND));
        updateMessage.update(request.getNewContent());

        return this.toMessageDto(updateMessage);
    }


    private MessageDto toMessageDto(Message message) {
        User user = message.getAuthor();
        Channel channel = message.getChannel();
        List<BinaryContent> contents = message.getAttachments();
        UserDto userDto = userMapper.toDto(user);
        List<BinaryContentDto> binaryContentDtos = contents.stream().map(BinaryContentDto::of).toList();
        return MessageDto.to(message, userDto, channel.getId(), binaryContentDtos);
    }

    @Override
    public void delete(MessageIdRequestDto requestDto) {
        Message deleteMessage = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND));
        List<BinaryContent> deletedBinaryContent = deleteMessage.getAttachments().stream().toList();
        binaryContentRepository.deleteAll(deletedBinaryContent); // 수정 파일 Id 값들 전부 데이터 삭제
        deletedBinaryContent.stream().map(BaseEntity::getId)
                .forEach(binaryContentStorage::delete);
        messageRepository.delete(deleteMessage); // 메시지 삭제
    }
}
