package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;

@Getter
public class Channel extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name; // 이름

    public Channel(String name) {
        super();
        this.name = name;
    }

    public void changeName(String name) {
        this.name = name;
        super.changeUpdatedAt();
    }

    @Override
    public String toString() {
        return String.format("Channel ( \n" +
                        " id=%s, createdAt=%s, updateAt=%s \n" +
                        " name=%s \n" + ")",
                super.getId(), super.getCreatedAt(), super.getUpdatedAt(),
                this.name
        );
    }
}
