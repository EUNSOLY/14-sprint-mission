package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.FileStorageUtil;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final FileStorageUtil fileStorageUtil;


    @Override
    public void save(MessageCreateRequestDto requestDto) {
        userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("유효한 사용자가 아닙니다."));

        channelRepository.findById(requestDto.getChannelId())
                .orElseThrow(() -> new RuntimeException("유효한 채널이 아닙니다."));

        Message savedMessage = requestDto.toEntity();

        requestDto.getFilesContent().stream()
                .filter((dto -> !dto.getFile().isEmpty()))
                .forEach(binaryContent -> {
                    String imageName = fileStorageUtil.imageUpload(binaryContent.getFile());
                    BinaryContent result = binaryContent.toEntity(imageName);
                    BinaryContent savedBinaryContent = binaryContentRepository.save(result); // BinaryContent 저장
                    UUID contentUuid = savedBinaryContent.getId();
                    savedMessage.addAttachmentId(contentUuid);
                });
        messageRepository.save(savedMessage);
    }

    @Override
    public MessageRespnoseDto find(MessageIdRequestDto requestDto) {
        Message message = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("찾으시는 메세지가 존재하지 않습니다."));

        return MessageRespnoseDto.from(message);
    }

    @Override
    public List<MessageRespnoseDto> findByUserId(UserIdRequestDto requestDto) {
        userRepository.findById(requestDto.getId()).orElseThrow(() -> new RuntimeException("회원정보가 잘못 됬습니다."));

        return messageRepository.findByUserId(requestDto.getId())
                .stream().map(MessageRespnoseDto::from).toList();
    }

    @Override
    public List<MessageRespnoseDto> findByChannelIdAndUserId(UserIdRequestDto userRequestDto, ChannelIdRequestDto channelRequestDto) {
        userRepository.findById(userRequestDto.getId()).orElseThrow(() -> new RuntimeException("회원정보가 잘못 됬습니다."));
        channelRepository.findById(channelRequestDto.getId()).orElseThrow(() -> new RuntimeException("채널 정보가 잘못 됬습니다."));

        return messageRepository.findByChannelIdAndUserId(userRequestDto.getId(), channelRequestDto.getId())
                .stream().map(MessageRespnoseDto::from).toList();

    }

    @Override
    public List<MessageRespnoseDto> findAllByChannelId(ChannelIdRequestDto requestDto) {
        channelRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("채널 정보가 잘못 됬습니다."));

        return messageRepository.findByChannelId(requestDto.getId())
                .stream().map(MessageRespnoseDto::from).toList();
    }

    @Override
    public void update(MessageUpdateRequestDto request) {
        Message updateMessage = messageRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("수정 메세지가 존재하지 않습니다."));

        if (!request.getDeleteFileIds().isEmpty()) {
            request.getDeleteFileIds().forEach(uuid -> {
                BinaryContent binaryContent = binaryContentRepository.findById(uuid)
                        .orElseThrow(() -> new RuntimeException("컨텐츠가 존재하지 않습니다."));
                fileStorageUtil.deleteUploadImage(binaryContent.getImageUrl());
                binaryContentRepository.delete(uuid);// 수정 파일 Id 값들 전부 데이터 삭제
            });
            updateMessage.removeAttachmentIds(request.getDeleteFileIds()); // 메세지에도 Id 값들 제거
        }

        List<UUID> savedBinaryContentIds = request.getFilesContent().stream()
                .filter((dto -> !dto.getFile().isEmpty()))
                .map(binaryContent -> {
                    String imageName = fileStorageUtil.imageUpload(binaryContent.getFile());
                    BinaryContent result = binaryContent.toEntity(imageName);
                    BinaryContent savedBinaryContent = binaryContentRepository.save(result); // BinaryContent 저장
                    return savedBinaryContent.getId();
                }).toList();

        updateMessage.update(request.getMessage());
        updateMessage.addAttachmentIds(savedBinaryContentIds);
        messageRepository.update(updateMessage.getId(), updateMessage);
    }

    @Override
    public void delete(MessageIdRequestDto requestDto) {
        Message deleteMessage = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("삭제 할 메세지가 존재하지 않습니다."));

        deleteMessage.getAttachmentIds().forEach(binaryContentRepository::delete); // 수정 파일 Id 값들 전부 데이터 삭제
        messageRepository.delete(requestDto.getId()); // 메시지 삭제
    }
}
