package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public BasicMessageService(
            MessageRepository messageRepository,
            UserService basicUserService,
            ChannelService basicChannelService
    ) {
        this.messageRepository = messageRepository;
        this.userService = basicUserService;
        this.channelService = basicChannelService;
    }

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public Message find(UUID id) {
        Message findMessage = messageRepository.findById(id);
        if (Objects.isNull(findMessage)) {
            throw new RuntimeException("찾으시는 메세지가 존재하지 않습니다.");
        }

        return findMessage;
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        if (Objects.isNull(userService.find(userId))) {
            throw new RuntimeException("회원정보가 잘못 됬습니다.");
        }
        return messageRepository.findByUserId(userId);
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        if (Objects.isNull(channelService.find(channelId))) {
            throw new RuntimeException("채널 정보가 잘못 됬습니다.");
        }
        return messageRepository.findByChannelId(channelId);
    }

    @Override
    public List<Message> findByChannelIdAndUserId(UUID userId, UUID channelId) {
        if (Objects.isNull(userService.find(userId)) || Objects.isNull(channelService.find(channelId))) {
            throw new RuntimeException("회원정보 또는 채널 정보가 잘못 됬습니다.");
        }

        return messageRepository.findByChannelIdAndUserId(userId, channelId);

    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public void update(UUID id, Message message) {
        Message findMessage = messageRepository.findById(id);
        if (Objects.isNull(findMessage)) {
            throw new RuntimeException("수정 할 메세지가 존재하지 않습니다.");
        }
        messageRepository.update(id, message);
    }

    @Override
    public void delete(UUID id) {
        Message findMessage = messageRepository.findById(id);
        if (Objects.isNull(findMessage)) {
            throw new RuntimeException("삭제 할 메세지가 존재하지 않습니다.");
        }
        messageRepository.delete(id);
    }
}
