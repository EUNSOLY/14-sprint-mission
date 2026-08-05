package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    private String message;
    private final UUID userId;
    private final UUID channelId;
    private List<UUID> attachmentIds;

    public Message(String message, UUID userId, UUID channelId) {
        super();
        this.message = message;
        this.userId = userId;
        this.channelId = channelId;
    }

    public void update(String message) {
        if (message != null) {
            this.message = message;
            super.updatedAt();
        }
    }

    public void addAttachmentId(UUID attachmentId) {
        this.attachmentIds.add(attachmentId);
    }

    public void addAttachmentIds(List<UUID> attachmentIds) {
        this.attachmentIds.addAll(attachmentIds);
    }

    public void removeAttachmentIds(List<UUID> attachmentIds) {
        attachmentIds.forEach(id -> this.attachmentIds.remove(id));
    }

    @Override
    public String toString() {
        return String.format("Message ( \n" +
                        " id=%s, createdAt=%s, updateAt=%s \n" +
                        " name=%s, userId=%s, channelId=%s \n" + ")",
                super.getId(), super.getCreatedAt(), super.getUpdatedAt(),
                this.message, this.userId, this.channelId
        );
    }
}