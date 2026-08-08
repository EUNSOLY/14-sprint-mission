package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.BinaryContentIdRequestDto;
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
    public BinaryContent save(BinaryContentCreateRequestDto requestDto) {
        return this.binaryContentRepository.save(requestDto.toEntity());
    }

    @Override
    public BinaryContent find(BinaryContentIdRequestDto requestDto) {
        return this.binaryContentRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("데이터가 존재하지 않습니다."));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<BinaryContentIdRequestDto> requestDto) {
        List<UUID> ids = requestDto.stream().map(IdRequestDto::getId).toList();
        return this.binaryContentRepository.findAllByIdIn(ids);
    }

    @Override
    public void delete(BinaryContentIdRequestDto requestDto) {
        this.binaryContentRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("삭제 할 데이터가 존재하지 않습니다."));

        this.binaryContentRepository.delete(requestDto.getId());
    }

}
