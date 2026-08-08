package com.sprint.mission.discodeit.service.basic;

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

    @Override
    public void save(MessageCreateRequestDto requestDto) {
        userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("유효한 사용자가 아닙니다."));

        channelRepository.findById(requestDto.getChannelId())
                .orElseThrow(() -> new RuntimeException("유효한 채널이 아닙니다."));

        Message savedMessage = requestDto.toEntity();

        if (!requestDto.getFiles().isEmpty()) {
            List<UUID> savedBinaryContentIds = requestDto.getFiles().stream()
                    .map(BinaryContent::new)
                    .map(binaryContent -> {
                        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
                        return savedBinaryContent.getId();
                    }).toList();

            savedMessage.addAttachmentIds(savedBinaryContentIds);
        }

        messageRepository.save(savedMessage);
    }

    @Override
    public Message find(MessageIdRequestDto requestDto) {
        return messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("찾으시는 메세지가 존재하지 않습니다."));
    }

    @Override
    public List<Message> findByUserId(UserIdRequestDto requestDto) {
        userRepository.findById(requestDto.getId()).orElseThrow(() -> new RuntimeException("회원정보가 잘못 됬습니다."));

        return messageRepository.findByUserId(requestDto.getId());
    }

    @Override
    public List<Message> findByChannelIdAndUserId(UserIdRequestDto userRequestDto, ChannelIdRequestDto channelRequestDto) {
        userRepository.findById(userRequestDto.getId()).orElseThrow(() -> new RuntimeException("회원정보가 잘못 됬습니다."));
        channelRepository.findById(channelRequestDto.getId()).orElseThrow(() -> new RuntimeException("채널 정보가 잘못 됬습니다."));


        return messageRepository.findByChannelIdAndUserId(userRequestDto.getId(), channelRequestDto.getId());

    }

    @Override
    public List<Message> findAllByChannelId(ChannelIdRequestDto requestDto) {
        channelRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("채널 정보가 잘못 됬습니다."));

        return messageRepository.findByChannelId(requestDto.getId());
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
    public void delete(MessageIdRequestDto requestDto) {
        Message deleteMessage = messageRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("삭제 할 메세지가 존재하지 않습니다."));

        deleteMessage.getAttachmentIds().forEach(binaryContentRepository::delete); // 수정 파일 Id 값들 전부 데이터 삭제
        messageRepository.delete(requestDto.getId()); // 메시지 삭제
    }
}
