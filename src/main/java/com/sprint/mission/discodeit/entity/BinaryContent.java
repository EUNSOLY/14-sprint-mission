package com.sprint.mission.discodeit.entity;

import lombok.Getter;

@Getter
public class BinaryContent extends BaseEntity {
    private final String fileName;
    private final byte[] bytes;
    private final String path;

    public BinaryContent(String fileName, byte[] bytes, String path) {
        this.fileName = fileName;
        this.bytes = bytes;
        this.path = path;
    }

    @Override
    public void updatedAt() {
        throw new RuntimeException("업데이트가 불가능합니다.");
    }

}
