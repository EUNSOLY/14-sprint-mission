package com.sprint.mission.discodeit.entity.binarycontent;

import com.sprint.mission.discodeit.entity.common.BaseEntity;
import lombok.Getter;

@Getter
public class BinaryContent extends BaseEntity {
    private final String fileName;
    private final byte[] bytes;
    private final String contentType;
    private final Long size;

    public BinaryContent(
            String fileName,
            byte[] bytes,
            String contentType,
            Long size
    ) {
        this.fileName = fileName;
        this.bytes = bytes;
        this.contentType = contentType;
        this.size = size;

    }

    @Override
    public void updatedAt() {
        throw new RuntimeException("업데이트가 불가능합니다.");
    }

}
