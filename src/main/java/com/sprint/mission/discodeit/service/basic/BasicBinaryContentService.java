package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.FileStorageUtil;
import com.sprint.mission.discodeit.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.BinaryContentIdRequestDto;
import com.sprint.mission.discodeit.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.IdRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final FileStorageUtil fileStorageUtil;


    @Override
    public BinaryContentResponseDto save(BinaryContentCreateRequestDto requestDto) {
        String imageName = fileStorageUtil.imageUpload(requestDto.getFileName(), requestDto.getBytes());
        BinaryContent savedContent = this.binaryContentRepository.save(requestDto.toEntity(imageName));

        return BinaryContentResponseDto.from(savedContent);
    }


    @Override
    public BinaryContentResponseDto find(BinaryContentIdRequestDto requestDto) {
        return this.binaryContentRepository.findById(requestDto.getId())
                .map(BinaryContentResponseDto::from)
                .orElseThrow(() -> new NoSuchElementException("데이터가 존재하지 않습니다."));
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<BinaryContentIdRequestDto> requestDto) {
        List<UUID> ids = requestDto.stream().map(IdRequestDto::getId).toList();
        return this.binaryContentRepository.findAllByIdIn(ids)
                .stream().map(BinaryContentResponseDto::from).toList();
    }

    @Override
    public void delete(BinaryContentIdRequestDto requestDto) {
        this.binaryContentRepository.findById(requestDto.getId())
                .orElseThrow(() -> new NoSuchElementException("삭제 할 데이터가 존재하지 않습니다."));

        this.binaryContentRepository.delete(requestDto.getId());
    }

}
