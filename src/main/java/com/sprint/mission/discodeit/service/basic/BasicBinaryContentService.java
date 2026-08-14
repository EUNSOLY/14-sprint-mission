package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.error.dto.ErrorCode;
import com.sprint.mission.discodeit.common.error.exception.GlobalCustomException;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public BinaryContentResponseDto save(BinaryContentCreateRequestDto requestDto) {
        BinaryContent savedContent = this.binaryContentRepository.save(requestDto.toEntity());

        return BinaryContentResponseDto.from(savedContent);
    }


    @Override
    public BinaryContentResponseDto find(BinaryContentIdRequestDto requestDto) {
        return this.binaryContentRepository.findById(requestDto.getId())
                .map(BinaryContentResponseDto::from)
                .orElseThrow(() -> new GlobalCustomException(ErrorCode.CONTENT_FILE_NOT_FOUND));
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
                .orElseThrow(() -> new GlobalCustomException(ErrorCode.CONTENT_FILE_NOT_FOUND, String.format("id = %s", requestDto.getId())));

        this.binaryContentRepository.delete(requestDto.getId());
    }
}
