package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
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

        User user = userValidator.getOrThrow(request.userId());
        Channel channel = channelValidator.getOrThrow(request.channelId());

        return this.readStatusRepository.findByUserIdAndChannelId(request.userId(), request.channelId())
                .orElseGet(() -> {
                    ReadStatus savedReadStatus = ReadStatus.create(user, channel);
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

        return this.readStatusRepository.update(updateReadStatus);
    }

    @Override
    public void delete(ReadStatusIdRequestDto request) {
        this.readStatusRepository.findById(request.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));

        this.readStatusRepository.delete(request.getId());
    }
}
