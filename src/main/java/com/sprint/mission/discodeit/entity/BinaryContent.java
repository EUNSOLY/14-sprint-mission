package com.sprint.mission.discodeit.entity;

import lombok.Getter;

@Getter
public class BinaryContent extends BaseEntity {
    private final Byte[] bytes;

    public BinaryContent(Byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public void updatedAt() {
        throw new RuntimeException("업데이트가 불가능합니다.");
    }
    
}
