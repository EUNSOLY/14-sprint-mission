package com.sprint.mission.discodeit.common;

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

    public String imageUpload(String originalFileName, byte[] bytes) {
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + originalFileName;
        System.out.println("이미지 이름: " + imageFileName);
        Path imageFilePath = Paths.get(uploadFolder, imageFileName);
        try {
            Files.write(imageFilePath, bytes);
        } catch (Exception e) {
            throw new RuntimeException("이미지를 저장할 수 없습니다.", e);
        }

        return imageFilePath.toFile().getName();
    }

    public void deleteUploadImage(String filePath) {
        System.out.println("삭제 파일 경로 : " + filePath);
        Path imageFilePath = Paths.get(uploadFolder, filePath);

        try {
            Files.delete(imageFilePath);
        } catch (Exception e) {
            throw new RuntimeException("이미지를 삭제할 수 없습니다.", e);
        }
    }
}
