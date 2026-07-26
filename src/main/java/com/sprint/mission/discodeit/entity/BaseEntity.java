package com.sprint.mission.discodeit.entity;
//import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

//@Getter
public class BaseEntity implements Serializable {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public void changeUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return this.id;
    }

    public Long getCreatedAt() {
        return this.createdAt;
    }

    public Long getUpdatedAt() {
        return this.updatedAt;
    }
}
