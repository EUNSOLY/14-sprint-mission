package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusIdRequestDto;
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
    public ReadStatus save(ReadStatusCreateRequestDto request) {

        userValidator.getOrThrow(request.getUserId());
        channelValidator.getOrThrow(request.getChannelId());
        
        return this.readStatusRepository.findByUserIdAndChannelId(request.getUserId(), request.getChannelId())
                .orElseGet(() -> {
                    ReadStatus savedReadStatus = request.toEntity();
                    this.readStatusRepository.save(savedReadStatus);
                    return savedReadStatus;
                });
    }

    @Override
    public ReadStatus find(ReadStatusIdRequestDto requestDto) {
        return this.readStatusRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UserIdRequestDto requestDto) {
        return this.readStatusRepository.findByUserId(requestDto.getId())
                .stream().toList();
    }

    @Override
    public ReadStatus update(ReadStatusIdRequestDto requestIdDto) {
        ReadStatus updateReadStatus = this.readStatusRepository.findById(requestIdDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));


        updateReadStatus.updateLastReadMessageAt();

        ReadStatus readStatus = this.readStatusRepository.update(updateReadStatus);
        return readStatus;
    }

    @Override
    public void delete(ReadStatusIdRequestDto request) {
        this.readStatusRepository.findById(request.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));

        this.readStatusRepository.delete(request.getId());
    }
}
