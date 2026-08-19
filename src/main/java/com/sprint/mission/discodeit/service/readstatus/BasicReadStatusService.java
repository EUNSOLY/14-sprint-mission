package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.error.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.channel.ChannelValidator;
import com.sprint.mission.discodeit.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
                    throw new GlobalCustomException(CustomStatusCode.DUPLICATE_DATA);

                });

        this.readStatusRepository.save(request.toEntity()); // 저장
    }

    @Override
    public ReadStatusResponseDto find(ReadStatusIdRequestDto requestDto) {
        return this.readStatusRepository.findById(requestDto.getId())
                .map(ReadStatusResponseDto::from)
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UserIdRequestDto requestDto) {
        return this.readStatusRepository.findByUserId(requestDto.getId())
                .stream().map(ReadStatusResponseDto::from).toList();

    }

    @Override
    public ReadStatusResponseDto update(ReadStatusUpdateRequestDto requestDto) {
        ReadStatus updateReadStatus = this.readStatusRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));


        updateReadStatus.updateLastReadMessageAt();

        ReadStatus readStatus = this.readStatusRepository.update(updateReadStatus);
        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public void delete(ReadStatusIdRequestDto request) {
        this.readStatusRepository.findById(request.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));

        this.readStatusRepository.delete(request.getId());
    }
}
