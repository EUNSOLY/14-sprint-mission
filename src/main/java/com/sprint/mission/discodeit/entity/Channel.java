package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;

@Getter
public class Channel extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    private ChannelType type;
    private String name;
    private String description;

    public Channel(ChannelType type, String name, String description) {
        super();
        //
        this.type = type;
        this.name = name;
        this.description = description;
    }


    public void update(String newName, String newDescription) {
        boolean anyValueUpdated = false;

        if (newName != null) {
            this.name = newName;
            anyValueUpdated = true;
        }

        if (newDescription != null) {
            this.description = newDescription;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            super.updatedAt();
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Channel{id=%s, type=%s, name='%s', description='%s', createdAt=%s, updatedAt=%s}",
                super.getId(), this.type, this.name, this.description, super.getCreatedAt(), super.getUpdatedAt()
        );
    }
}
