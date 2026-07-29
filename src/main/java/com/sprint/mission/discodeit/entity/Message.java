package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    private String message; // 이름
    private final UUID userId;
    private final UUID channelId;


    public Message(String message, UUID userId, UUID channelId) {
        super();
        this.message = message;
        this.userId = userId;
        this.channelId = channelId;
    }

    public void changeMessage(String message) {
        this.message = message;
        super.changeUpdatedAt();
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