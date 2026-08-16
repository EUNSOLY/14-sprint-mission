package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.error.exception.GlobalCustomException;
import com.sprint.mission.discodeit.common.validator.BinaryContentValidator;
import com.sprint.mission.discodeit.common.validator.ChannelValidator;
import com.sprint.mission.discodeit.common.validator.UserValidator;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
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
    public void save(
            MessageCreateRequestDto requestDto,
            List<BinaryContentCreateRequestDto> messageContentCreateRequests
    ) {

        userValidator.getOrThrow(requestDto.getUserId());
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
    }

    @Override
    public MessageResponseDto find(MessageIdRequestDto requestDto) {
        Message message = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND));

        return MessageResponseDto.from(message);
    }

    @Override
    public List<MessageResponseDto> findByUserId(UserIdRequestDto requestDto) {
        userValidator.getOrThrow(requestDto.getId());

        return messageRepository.findByUserId(requestDto.getId())
                .stream().map(MessageResponseDto::from).toList();
    }

    @Override
    public List<MessageResponseDto> findByChannelIdAndUserId(UserIdRequestDto userRequestDto, ChannelIdRequestDto channelRequestDto) {

        userValidator.getOrThrow(userRequestDto.getId());
        channelValidator.getOrThrow(channelRequestDto.getId());

        return messageRepository.findByChannelIdAndUserId(userRequestDto.getId(), channelRequestDto.getId())
                .stream().map(MessageResponseDto::from).toList();

    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(ChannelIdRequestDto requestDto) {
        channelValidator.getOrThrow(requestDto.getId());

        return messageRepository.findByChannelId(requestDto.getId())
                .stream().map(MessageResponseDto::from).toList();
    }

    @Override
    public void update(
            MessageUpdateRequestDto request,
            List<BinaryContentCreateRequestDto> messageContentCreateRequests
    ) {
        Message updateMessage = messageRepository.findById(request.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND));


        Optional.ofNullable(request.getDeleteFileIds())
                .ifPresent(deleteContentIds -> {
                    deleteContentIds.forEach(deleteId -> {
                        binaryContentValidator.getOrThrow(deleteId);
                        binaryContentRepository.delete(deleteId);
                    });
                });

        List<UUID> saveContentIds = Optional.ofNullable(messageContentCreateRequests)
                .filter(list -> !list.isEmpty())
                .map(messageBinaryContents -> {
                    return messageBinaryContents.stream().map(messageBinaryContent -> {
                        BinaryContent binaryContent = messageBinaryContent.toEntity();
                        return binaryContentRepository.save(binaryContent).getId();
                    }).toList();
                })
                .orElse(Collections.emptyList());


        updateMessage.addAttachmentIds(saveContentIds);
        updateMessage.update(request.getMessage());
        messageRepository.update(updateMessage.getId(), updateMessage);
    }

    @Override
    public void delete(MessageIdRequestDto requestDto) {
        Message deleteMessage = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND));


        deleteMessage.getAttachmentIds().forEach(binaryContentRepository::delete); // 수정 파일 Id 값들 전부 데이터 삭제
        messageRepository.delete(requestDto.getId()); // 메시지 삭제
    }
}
