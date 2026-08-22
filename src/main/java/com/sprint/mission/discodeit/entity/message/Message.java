package com.sprint.mission.discodeit.entity.message;

import com.sprint.mission.discodeit.entity.common.BaseEntity;
import lombok.Getter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    private String content;
    private final UUID authorId;
    private final UUID channelId;
    private final List<UUID> attachmentIds;

    public Message(String message, UUID userId, UUID channelId) {
        super();
        this.content = message;
        this.authorId = userId;
        this.channelId = channelId;
        this.attachmentIds = new ArrayList<>();
    }

    public void update(String message) {
        if (message != null) {
            this.content = message;
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
                this.content, this.authorId, this.channelId
        );
    }
}