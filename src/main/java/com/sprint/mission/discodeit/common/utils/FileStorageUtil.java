package com.sprint.mission.discodeit.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileStorageUtil {
    //파일 업로드 경로 지정
    private final String uploadFolder;

    public FileStorageUtil(@Value("${file.path}") String uploadFolder) {
        this.uploadFolder = uploadFolder;
    }

    public String saveFile(String originalFileName, byte[] bytes) {
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + originalFileName;
        System.out.println("파일 이름: " + imageFileName);
        Path imageFilePath = Paths.get(uploadFolder, imageFileName);
        try {
            Files.write(imageFilePath, bytes);
        } catch (Exception e) {
            throw new RuntimeException("파일을 저장할 수 없습니다.", e);
        }

        return imageFilePath.toFile().getName();
    }

    public void deleteFile(String filePath) {
        System.out.println("삭제 파일 경로 : " + filePath);
        Path imageFilePath = Paths.get(uploadFolder, filePath);

        try {
            Files.delete(imageFilePath);
        } catch (Exception e) {
            throw new RuntimeException("파일을 삭제할 수 없습니다.", e);
        }
    }
}
