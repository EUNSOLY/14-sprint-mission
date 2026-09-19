package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;


    @Override
    public ChannelDto save(PublicChannelCreateRequestDto request) {
        Channel savedChannel = channelRepository.save(request.toEntity());
        Instant messageLastTime = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(savedChannel.getId())
                .map(BaseEntity::getCreatedAt)
                .orElse(null);
        return ChannelDto.of(savedChannel, List.of(), messageLastTime);
    }

    @Override
    @Transactional
    public ChannelDto save(PrivateChannelCreateRequestDto request) {
        Channel savedChannel = Channel.create(ChannelType.PRIVATE, "", "");
        List<UUID> userIds = request.getParticipantIds();
        Channel savedEntity = channelRepository.save(savedChannel);

        // 사용자별 ReadStatus 생성
        userIds.forEach(userId -> {
            System.out.println(userId);
            User user = userValidator.getOrThrow(userId);
            ReadStatus readStatus = ReadStatus.create(user, savedEntity);
            readStatusRepository.save(readStatus);
        });


        return this.toChannelDto(savedEntity);
    }

    @Override
    public ChannelDto find(ChannelIdRequestDto requestDto) {
        return channelRepository.findById(requestDto.getId())
                .map(this::toChannelDto)
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.CHANNEL_NOT_FOUND));


    }

    public List<ChannelDto> findAll() {
        return channelRepository.findAll()
                .stream()
                .map(this::toChannelDto).toList();
    }

    @Override
    public List<ChannelDto> findAllByUserId(UserIdRequestDto requestDto) {
        return channelRepository.findAll().stream()
                .filter(channel -> {
                    // 공개 채널은 통과
                    if (channel.getType().equals(ChannelType.PUBLIC)) {
                        return true;
                    }

                    // 비공개 채널이면 user가 포함된 채널만 통과
                    return readStatusRepository.findByChannelId(channel.getId())
                            .stream().anyMatch(readStatus -> readStatus.getUser().getId().equals(requestDto.getId()));
                })
                .map(this::toChannelDto)
                .toList();
    }

    private ChannelDto toChannelDto(Channel channel) {
        Instant messageLastTime = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channel.getId())
                .map(BaseEntity::getCreatedAt)
                .orElse(null);

        List<UserDto> users = readStatusRepository.findByChannelId(channel.getId()).stream()
                .map(ReadStatus::getUser)
                .map(userMapper::toDto)
                .toList();

        return ChannelDto.of(channel, users, messageLastTime);
    }

    @Override
    @Transactional
    public ChannelDto update(ChannelIdRequestDto channelId, ChannelUpdateRequestDto requestDto) {
        Channel updateChannel = channelRepository.findById(channelId.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.CHANNEL_NOT_FOUND));

        if (updateChannel.getType().equals(ChannelType.PRIVATE)) {
            throw new GlobalCustomException(CustomStatusCode.PRIVATE_CHANNEL_CANNOT_UPDATE);
        }

        updateChannel.update(requestDto.getNewName(), requestDto.getNewDescription());
        return this.toChannelDto(updateChannel);
    }

    @Override
    @Transactional
    public void delete(ChannelIdRequestDto requestDto) {
        Channel deleteChannel = channelRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.CHANNEL_NOT_FOUND));

        channelRepository.delete(deleteChannel);
    }
}
