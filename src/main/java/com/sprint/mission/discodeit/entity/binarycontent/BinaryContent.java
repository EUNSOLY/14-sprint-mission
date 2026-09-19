package com.sprint.mission.discodeit.entity.binarycontent;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "binary_contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {
    private String fileName;
    private String contentType;
    private Long size;

    private BinaryContent(
            String fileName,
            String contentType,
            Long size
    ) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
    }

    public static BinaryContent create(
            String fileName,
            String contentType,
            Long size
    ) {
        return new BinaryContent(fileName, contentType, size);
    }
}
