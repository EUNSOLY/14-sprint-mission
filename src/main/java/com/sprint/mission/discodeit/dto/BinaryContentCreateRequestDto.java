package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class BinaryContentCreateRequestDto {
    private final MultipartFile file;
    private final String title;

    public BinaryContent toEntity(String imageName) {
        return new BinaryContent(this.title, imageName);
    }
}
