package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.channel.ChannelValidator;
import com.sprint.mission.discodeit.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final ChannelValidator channelValidator;
    private final UserValidator userValidator;

    @Override
    public ReadStatusDto save(ReadStatusCreateRequestDto request) {

        User user = userValidator.getOrThrow(request.userId());
        Channel channel = channelValidator.getOrThrow(request.channelId());

        ReadStatus readStatus = this.readStatusRepository.findByUserIdAndChannelId(request.userId(), request.channelId())
                .orElseGet(() -> {
                    ReadStatus savedReadStatus = ReadStatus.create(user, channel);
                    this.readStatusRepository.save(savedReadStatus);
                    return savedReadStatus;
                });

        return this.toReadStatusDto(readStatus, user, channel);
    }

    @Override
    public ReadStatusDto find(ReadStatusIdRequestDto requestDto) {
        ReadStatus readStatus = this.readStatusRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ReadStatusNotFoundException(Map.of("읽음 상태 ID", requestDto.getId())));

        User user = readStatus.getUser();
        Channel channel = readStatus.getChannel();

        return this.toReadStatusDto(readStatus, user, channel);
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UserIdRequestDto requestDto) {
        List<ReadStatus> readStatus = this.readStatusRepository.findByUserId(requestDto.getId())
                .stream().toList();

        return readStatus.stream().map(readStatus1 -> {
            User user = readStatus1.getUser();
            Channel channel = readStatus1.getChannel();

            return this.toReadStatusDto(readStatus1, user, channel);
        }).toList();
    }

    @Override
    @Transactional
    public ReadStatusDto update(ReadStatusIdRequestDto requestIdDto) {
        ReadStatus updateReadStatus = this.readStatusRepository.findById(requestIdDto.getId())
                .orElseThrow(() -> new ReadStatusNotFoundException(Map.of("읽음 상태 ID", requestIdDto.getId())));

        updateReadStatus.updateLastReadMessageAt();

        User user = updateReadStatus.getUser();
        Channel channel = updateReadStatus.getChannel();
        return this.toReadStatusDto(updateReadStatus, user, channel);
    }

    private ReadStatusDto toReadStatusDto(ReadStatus readStatus, User user, Channel channel) {
        return ReadStatusDto.to(readStatus, user.getId(), channel.getId());
    }

    @Override
    public void delete(ReadStatusIdRequestDto request) {
        ReadStatus deletedEntity = this.readStatusRepository.findById(request.getId())
                .orElseThrow(() -> new ReadStatusNotFoundException(Map.of("읽음 상태 ID", request.getId())));

        this.readStatusRepository.delete(deletedEntity);
    }
}
