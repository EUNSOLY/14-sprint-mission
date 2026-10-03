package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentIdRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.common.IdRequestDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;


    @Override
    public BinaryContent save(BinaryContentCreateRequestDto requestDto) {
        BinaryContent savedBinaryContent = this.binaryContentRepository.save(requestDto.toEntity());
        binaryContentStorage.put(savedBinaryContent.getId(), requestDto.bytes());
        return savedBinaryContent;
    }


    @Override
    public BinaryContent find(BinaryContentIdRequestDto requestDto) {
        return this.binaryContentRepository.findById(requestDto.getId())
                .orElseThrow(() -> new BinaryContentNotFoundException(Map.of("파일 ID", requestDto.getId())));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<BinaryContentIdRequestDto> requestDto) {
        List<UUID> ids = requestDto.stream().map(IdRequestDto::getId).toList();
        return this.binaryContentRepository.findByIdIn(ids)
                .stream().toList();
    }

    @Override
    public void delete(BinaryContentIdRequestDto requestDto) {
        BinaryContent deletedEntity = this.binaryContentRepository.findById(requestDto.getId())
                .orElseThrow(() -> new BinaryContentNotFoundException(Map.of("파일 ID", requestDto.getId())));

        this.binaryContentRepository.delete(deletedEntity);
    }

    @Override
    public ResponseEntity<?> findFile(UUID fileId) {
        BinaryContent binaryContent = binaryContentRepository.findById(fileId)
                .orElse(null);

        if (binaryContent == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        BinaryContentDto binaryContentDto = BinaryContentDto.of(binaryContent);

        return binaryContentStorage.download(binaryContentDto);
    }
}
