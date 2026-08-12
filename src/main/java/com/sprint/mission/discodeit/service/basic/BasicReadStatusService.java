package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.validator.ChannelValidator;
import com.sprint.mission.discodeit.common.validator.UserValidator;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final ChannelValidator channelValidator;
    private final UserValidator userValidator;

    @Override
    public void save(ReadStatusCreateRequestDto request) {

        userValidator.getOrThrow(request.getUserId());
        channelValidator.getOrThrow(request.getChannelId());

        // ifPresent : 값이 있다면 실행
        this.readStatusRepository.findByUserIdAndChannelId(request.getUserId(), request.getChannelId())
                .ifPresent(status -> {
                    throw new IllegalStateException("이미 존재하는 데이터입니다. 신규로 생성하실 수 없습니다.");
                });

        this.readStatusRepository.save(request.toEntity()); // 저장
    }

    @Override
    public ReadStatusResponseDto find(ReadStatusIdRequestDto requestDto) {
        return this.readStatusRepository.findById(requestDto.getId())
                .map(ReadStatusResponseDto::from)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 데이터 입니다."));
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UserIdRequestDto requestDto) {
        return this.readStatusRepository.findByUserId(requestDto.getId())
                .stream().map(ReadStatusResponseDto::from).toList();

    }

    @Override
    public ReadStatusResponseDto update(ReadStatusUpdateRequestDto requestDto) {
        ReadStatus updateReadStatus = this.readStatusRepository.findById(requestDto.getId())
                .orElseThrow(() -> new NoSuchElementException("수정이 가능한 데이터가 존재하지 않습니다."));

        updateReadStatus.updateLastReadMessageAt();

        ReadStatus readStatus = this.readStatusRepository.update(updateReadStatus);
        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public void delete(ReadStatusIdRequestDto request) {
        this.readStatusRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("삭제 가능한 데이터가 존재하지 않습니다."));

        this.readStatusRepository.delete(request.getId());
    }
}
