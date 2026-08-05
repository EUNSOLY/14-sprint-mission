package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequestDto;
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

    @Override
    public void save(MessageCreateRequestDto requestDto) {
        userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("유효한 사용자가 아닙니다."));

        channelRepository.findById(requestDto.getChannelId())
                .orElseThrow(() -> new RuntimeException("유효한 채널이 아닙니다."));

        Message savedMessage = requestDto.toEntity();
        List<UUID> savedBinaryContentIds = requestDto.getFiles().stream()
                .map(BinaryContent::new)
                .map(binaryContent -> {
                    BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
                    return savedBinaryContent.getId();
                }).toList();

        savedMessage.addAttachmentIds(savedBinaryContentIds);
        messageRepository.save(savedMessage);
    }

    @Override
    public Message find(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("찾으시는 메세지가 존재하지 않습니다."));
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("회원정보가 잘못 됬습니다."));

        return messageRepository.findByUserId(userId);
    }

    @Override
    public List<Message> findByChannelIdAndUserId(UUID userId, UUID channelId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("회원정보가 잘못 됬습니다."));
        channelRepository.findById(channelId).orElseThrow(() -> new RuntimeException("채널 정보가 잘못 됬습니다."));


        return messageRepository.findByChannelIdAndUserId(userId, channelId);

    }

    @Override
    public List<Message> findallByChannelId(UUID channelId) {
        channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("채널 정보가 잘못 됬습니다."));

        return messageRepository.findByChannelId(channelId);
    }

    @Override
    public void update(MessageUpdateRequestDto request) {
        Message updateMessage = messageRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("수정 메세지가 존재하지 않습니다."));

        request.getDeleteFileIds().forEach(binaryContentRepository::delete); // 수정 파일 Id 값들 전부 데이터 삭제
        updateMessage.removeAttachmentIds(request.getDeleteFileIds()); // 메세지에도 Id 값들 제거

        List<UUID> newFileIds = request.getFiles()
                .stream()
                .map(BinaryContent::new)
                .map(binaryContent -> {
                    binaryContentRepository.save(binaryContent);
                    return binaryContent.getId();
                })
                .toList();

        updateMessage.update(request.getMessage());
        messageRepository.update(updateMessage.getId(), updateMessage);
    }

    @Override
    public void delete(UUID id) {
        Message deleteMessage = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("삭제 할 메세지가 존재하지 않습니다."));

        deleteMessage.getAttachmentIds().forEach(binaryContentRepository::delete); // 수정 파일 Id 값들 전부 데이터 삭제
        messageRepository.delete(id); // 메시지 삭제
    }
}
