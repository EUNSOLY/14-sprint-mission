package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Getter
@Entity
@Table(name = "channels")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    public static Channel create(ChannelType type, String name, String description) {
        return new Channel(type, name, description);
    }

    private Channel(ChannelType type, String name, String description) {
        super();
        this.type = type;
        this.name = name;
        this.description = description;
    }


    public void update(String newName, String newDescription) {
        if (newName != null) {
            this.name = newName;
        }

        if (newDescription != null) {
            this.description = newDescription;
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
