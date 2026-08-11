package com.sprint.mission.discodeit.repository.file;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class FileAbstractRepository {
    @Value("${discodeit.repository.file-directory}")
    private String uploadFolder;
    private final String fileName;

    protected FileAbstractRepository(String fileName) {
        this.fileName = fileName;
    }

    @PostConstruct
    private void initUploadFolder() {
        try {
            assert uploadFolder != null;
            Path directory = Paths.get(uploadFolder);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

        } catch (IOException e) {
            throw new RuntimeException("업로드 폴더 생성 실패", e);
        }
    }

    protected <T> void fileSave(Map<UUID, T> data) {
        Path uploadPath = Paths.get(uploadFolder, this.fileName);

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(uploadPath.toFile()))) {
            objectOutputStream.writeObject(data);
            System.out.println("객체 직렬화 변환 및 파일 저장 완료");
        } catch (IOException e) {
            System.out.println("오류 발생");
            throw new RuntimeException(e);
        }
    }

    protected <T> Map<UUID, T> load() {
        Path uploadPath = Paths.get(uploadFolder, this.fileName);

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(uploadPath.toFile()))) {
            Map<UUID, T> loadedMessage = (Map<UUID, T>) objectInputStream.readObject(); // 파일에서 객체 읽기
            System.out.println("역직렬화 완료" + loadedMessage.toString());
            return loadedMessage;

        } catch (FileNotFoundException e) {
            return new HashMap<>(); // 값이 없으면 Map 초기화
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("역직렬화 할 클래스파일이 존재하지않습니다.", e);
        } catch (IOException e) {
            throw new RuntimeException("데이터 파싱에 실패", e);
        }
    }
}
