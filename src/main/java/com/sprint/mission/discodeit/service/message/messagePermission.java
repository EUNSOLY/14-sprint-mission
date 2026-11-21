package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Component("messagePermission")
public class messagePermission {

    private final MessageRepository messageRepository;

    @Transactional(readOnly = true)
    public boolean isAuthor(UUID messageId, UUID userId) {
        return messageRepository.findById(messageId)
                .map(message -> message.getAuthor() != null && message.getAuthor().getId().equals(userId))
                .orElse(true);
    }
}
