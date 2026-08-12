package com.sprint.mission.discodeit.common.utils;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinaryContentMapper {
    public static BinaryContentCreateRequestDto to(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return new BinaryContentCreateRequestDto(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
    }

    public static List<BinaryContentCreateRequestDto> toList(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }
        List<BinaryContentCreateRequestDto> result = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                result.add(to(file));
            }
        }
        return result;
    }
}
