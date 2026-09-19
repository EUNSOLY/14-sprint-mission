package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {

    UUID put(UUID key, byte[] value);

    void delete(UUID key);

    InputStream get(UUID key);

    ResponseEntity<?> download(BinaryContentDto binaryContentDto);
}
