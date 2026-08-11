package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.FileStorageUtil;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final FileStorageUtil fileStorageUtil;
    private final ChannelValidator channelValidator;
    private final UserValidator userValidator;
    private final BinaryContentValidator binaryContentValidator;


    @Override
    public void save(MessageCreateRequestDto requestDto) {

        userValidator.getOrThrow(requestDto.getUserId());
        channelValidator.getOrThrow(requestDto.getChannelId());

        Message savedMessage = requestDto.toEntity();

        if (!requestDto.getFilesContent().isEmpty()) {
            requestDto.getFilesContent().forEach(binaryContent -> {
                String imageName = fileStorageUtil.imageUpload(binaryContent.getFileName(), binaryContent.getBytes());
                BinaryContent result = binaryContent.toEntity(imageName);
                BinaryContent savedBinaryContent = binaryContentRepository.save(result); // BinaryContent 저장
                UUID contentUuid = savedBinaryContent.getId();
                savedMessage.addAttachmentId(contentUuid);
            });
        }
        messageRepository.save(savedMessage);
    }

    @Override
    public MessageResponseDto find(MessageIdRequestDto requestDto) {
        Message message = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("찾으시는 메세지가 존재하지 않습니다."));

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
    public void update(MessageUpdateRequestDto request) {
        Message updateMessage = messageRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("수정할 메세지가 존재하지 않습니다."));

        if (!request.getDeleteFileIds().isEmpty()) {
            request.getDeleteFileIds().forEach(uuid -> {
                BinaryContent binaryContent = binaryContentValidator.getOrThrow(uuid);
                fileStorageUtil.deleteUploadImage(binaryContent.getPath());
                binaryContentRepository.delete(uuid);// 수정 파일 Id 값들 전부 데이터 삭제
            });
            updateMessage.removeAttachmentIds(request.getDeleteFileIds()); // 메세지에도 Id 값들 제거
        }

        if (!request.getFilesContent().isEmpty()) {
            List<UUID> savedBinaryContentIds = request.getFilesContent().stream()
                    .map(binaryContent -> {
                        String imageName = fileStorageUtil.imageUpload(binaryContent.getFileName(), binaryContent.getBytes());
                        BinaryContent result = binaryContent.toEntity(imageName);
                        BinaryContent savedBinaryContent = binaryContentRepository.save(result); // BinaryContent 저장
                        return savedBinaryContent.getId();
                    }).toList();
            updateMessage.addAttachmentIds(savedBinaryContentIds);
        }

        updateMessage.update(request.getMessage());
        messageRepository.update(updateMessage.getId(), updateMessage);
    }

    @Override
    public void delete(MessageIdRequestDto requestDto) {
        Message deleteMessage = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new NoSuchElementException("삭제 할 메세지가 존재하지 않습니다."));

        deleteMessage.getAttachmentIds().forEach(binaryContentRepository::delete); // 수정 파일 Id 값들 전부 데이터 삭제
        messageRepository.delete(requestDto.getId()); // 메시지 삭제
    }
}
