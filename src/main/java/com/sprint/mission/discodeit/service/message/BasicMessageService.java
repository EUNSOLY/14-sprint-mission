package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentValidator;
import com.sprint.mission.discodeit.service.channel.ChannelValidator;
import com.sprint.mission.discodeit.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ChannelValidator channelValidator;
    private final UserValidator userValidator;
    private final BinaryContentValidator binaryContentValidator;


    @Override
    public Message save(
            MessageCreateRequestDto requestDto,
            List<BinaryContentCreateRequestDto> messageContentCreateRequests
    ) {

        userValidator.getOrThrow(requestDto.getAuthorId());
        channelValidator.getOrThrow(requestDto.getChannelId());

        Message savedMessage = requestDto.toEntity();

        List<UUID> contentIds = Optional.ofNullable(messageContentCreateRequests)
                .filter(list -> !list.isEmpty())
                .map(messageBinaryContents -> {
                    return messageBinaryContents.stream().map(messageBinaryContent -> {
                        BinaryContent binaryContent = messageBinaryContent.toEntity();
                        return binaryContentRepository.save(binaryContent).getId();
                    }).toList();
                })
                .orElse(Collections.emptyList());

        savedMessage.addAttachmentIds(contentIds);
        messageRepository.save(savedMessage);
        return savedMessage;
    }

    @Override
    public Message find(MessageIdRequestDto requestDto) {
        return messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND));

    }

    @Override
    public List<Message> findByUserId(UserIdRequestDto requestDto) {
        userValidator.getOrThrow(requestDto.getId());

        return messageRepository.findByUserId(requestDto.getId())
                .stream().toList();
    }

    @Override
    public List<Message> findByChannelIdAndUserId(UserIdRequestDto userRequestDto, ChannelIdRequestDto channelRequestDto) {

        userValidator.getOrThrow(userRequestDto.getId());
        channelValidator.getOrThrow(channelRequestDto.getId());

        return messageRepository.findByChannelIdAndUserId(userRequestDto.getId(), channelRequestDto.getId())
                .stream().toList();

    }

    @Override
    public List<Message> findAllByChannelId(ChannelIdRequestDto requestDto) {
        channelValidator.getOrThrow(requestDto.getId());

        return messageRepository.findByChannelId(requestDto.getId())
                .stream().toList();
    }

    @Override
    public Message update(
            MessageIdRequestDto messageIdRequest,
            MessageUpdateRequestDto request
    ) {
        Message updateMessage = messageRepository.findById(messageIdRequest.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND));

        updateMessage.update(request.getNewContent());
        messageRepository.update(updateMessage.getId(), updateMessage);

        return updateMessage;
    }

    @Override
    public void delete(MessageIdRequestDto requestDto) {
        Message deleteMessage = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND));


        deleteMessage.getAttachmentIds().forEach(binaryContentRepository::delete); // 수정 파일 Id 값들 전부 데이터 삭제
        messageRepository.delete(requestDto.getId()); // 메시지 삭제
    }
}
