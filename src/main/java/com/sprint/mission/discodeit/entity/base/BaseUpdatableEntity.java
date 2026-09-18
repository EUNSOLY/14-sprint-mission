package com.sprint.mission.discodeit.entity.base;

import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
public abstract class BaseUpdatableEntity extends BaseEntity {
    @UpdateTimestamp
    private Instant updatedAt;

    public BaseUpdatableEntity() {
        super();
    }
}
