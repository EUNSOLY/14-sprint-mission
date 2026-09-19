package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.channel.ChannelValidator;
import com.sprint.mission.discodeit.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final ChannelValidator channelValidator;
    private final UserValidator userValidator;
    private final UserStatusRepository userStatusRepository;
    private final MessageRepository messageRepository;

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
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));

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
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));

        updateReadStatus.updateLastReadMessageAt();

        User user = updateReadStatus.getUser();
        Channel channel = updateReadStatus.getChannel();
        return this.toReadStatusDto(updateReadStatus, user, channel);
    }

    private ReadStatusDto toReadStatusDto(ReadStatus readStatus, User user, Channel channel) {
        Instant messageLastTime = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channel.getId())
                .map(BaseEntity::getCreatedAt)
                .orElse(null);

        List<UserDto> users = readStatusRepository.findByChannelId(channel.getId()).stream()
                .map(ReadStatus::getUser)
                .map(readUser -> {
                    boolean userStatus = userStatusRepository.findByUserId(readUser.getId())
                            .map(UserStatus::isOnline)
                            .orElse(false);

                    return UserDto.of(readUser, userStatus);
                })
                .toList();

        boolean userStatus = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(false);

        UserDto userDto = UserDto.of(user, userStatus);
        ChannelDto channelDto = ChannelDto.of(channel, users, messageLastTime);
        return ReadStatusDto.to(readStatus, user.getId(), channel.getId());
    }

    @Override
    public void delete(ReadStatusIdRequestDto request) {
        ReadStatus deletedEntity = this.readStatusRepository.findById(request.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));

        this.readStatusRepository.delete(deletedEntity);
    }
}
