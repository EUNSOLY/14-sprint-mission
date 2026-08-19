package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentIdRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.common.IdRequestDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.CONTENT_FILE_NOT_FOUND));
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
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.CONTENT_FILE_NOT_FOUND));

        this.binaryContentRepository.delete(requestDto.getId());
    }
}
