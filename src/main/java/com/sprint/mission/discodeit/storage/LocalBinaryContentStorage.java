package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.UUID;


@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local", matchIfMissing = true)
public class LocalBinaryContentStorage implements BinaryContentStorage {
    private final String uploadDirPath;

    public LocalBinaryContentStorage(
            @Value("${discodeit.storage.local.root-path}") String uploadDir
    ) {
        this.uploadDirPath = uploadDir;
        this.init();
    }

    public void init() {
        File directory = new File(uploadDirPath);
        if (!directory.exists()) {
            boolean createDir = directory.mkdirs();
        }
    }

    @Override
    public UUID put(UUID key, byte[] value) {
        Path path = this.resolvePath(key);
        try (FileOutputStream savedFile = new FileOutputStream(path.toString())) {
            savedFile.write(value);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
        return key;
    }

    @Override
    public void delete(UUID key) {
        Path path = this.resolvePath(key);
        File file = path.toFile();
        boolean delete = file.delete();
    }

    @Override
    public InputStream get(UUID key) {
        Path path = this.resolvePath(key);
        try {
            return new FileInputStream(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException("파일을 불러오는 도중 예외가 발생했습니다.", e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        InputStream contentStream = this.get(binaryContentDto.id());
        InputStreamResource inputStreamResource = new InputStreamResource(contentStream);
        return ResponseEntity.status(HttpStatus.OK)
                .contentLength(binaryContentDto.size())
                .contentType(MediaType.valueOf(binaryContentDto.contentType()))
                .body(inputStreamResource);
    }

    // 파일의 실제 저장 위치에 대한 규칙을 정의
    private Path resolvePath(UUID fileId) {
        Path baseDirPath = Path.of(uploadDirPath);
        Path filePath = baseDirPath.resolve(fileId.toString());

        log.info("테스트합니다: {}", filePath);
        return filePath;
    }
}