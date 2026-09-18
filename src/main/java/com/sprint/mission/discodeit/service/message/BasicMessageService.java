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
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.channel.ChannelValidator;
import com.sprint.mission.discodeit.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
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
    private final ChannelValidator channelValidator;
    private final UserValidator userValidator;


    @Override
    public Message save(
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
                        return binaryContentRepository.save(binaryContent);
                    }).toList();
                })
                .orElse(Collections.emptyList());

        savedMessage.addAttachments(contents);
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
        User user = userValidator.getOrThrow(requestDto.getId());

        return messageRepository.findByAuthorId(requestDto.getId())
                .stream().toList();
    }

    @Override
    public List<Message> findByChannelIdAndUserId(UserIdRequestDto userRequestDto, ChannelIdRequestDto channelRequestDto) {

        userValidator.getOrThrow(userRequestDto.getId());
        channelValidator.getOrThrow(channelRequestDto.getId());

        return messageRepository.findByChannelIdAndAuthorId(userRequestDto.getId(), channelRequestDto.getId())
                .stream().toList();

    }

    @Override
    public List<Message> findAllByChannelId(ChannelIdRequestDto requestDto) {
        channelValidator.getOrThrow(requestDto.getId());

        return messageRepository.findByChannelId(requestDto.getId())
                .stream().toList();
    }

    @Override
    @Transactional
    public Message update(
            MessageIdRequestDto messageIdRequest,
            MessageUpdateRequestDto request
    ) {
        Message updateMessage = messageRepository.findById(messageIdRequest.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND));
        updateMessage.update(request.getNewContent());
        return updateMessage;
    }

    @Override
    public void delete(MessageIdRequestDto requestDto) {
        Message deleteMessage = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.MESSAGE_NOT_FOUND));
        List<BinaryContent> deletedBinaryContent = deleteMessage.getAttachments().stream().toList();
        binaryContentRepository.deleteAll(deletedBinaryContent); // 수정 파일 Id 값들 전부 데이터 삭제
        messageRepository.delete(deleteMessage); // 메시지 삭제
    }
}
