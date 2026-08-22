package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.common.BaseEntity;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;


    @Override
    public Channel save(PublicChannelCreateRequestDto request) {
        Channel savedChannel = request.toEntity();
        channelRepository.save(savedChannel);

        return savedChannel;
    }

    @Override
    public Channel save(PrivateChannelCreateRequestDto request) {
        Channel savedChannel = request.toEntity();
        List<UUID> userIds = request.getParticipantIds();

        // 사용자별 ReadStatus 생성
        userIds.forEach(userId -> {
            ReadStatus readStatus = new ReadStatus(userId, savedChannel.getId());
            readStatusRepository.save(readStatus);
        });

        channelRepository.save(savedChannel);
        return savedChannel;
    }

    @Override
    public ChannelDto find(ChannelIdRequestDto requestDto) {
        return channelRepository.findById(requestDto.getId())
                .map(channel -> {
                    // 최신 메시지 시간 정보
                    Instant messageLastTime = messageRepository.findByChannelId(channel.getId()).stream()
                            .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                            .map(BaseEntity::getCreatedAt)
                            .findFirst().orElse(null);

                    List<UUID> userIds = readStatusRepository.findByChannelId(channel.getId()).stream()
                            .map(ReadStatus::getUserId)
                            .toList();
                    return ChannelDto.of(channel, userIds, messageLastTime);

                })
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.CHANNEL_NOT_FOUND));


    }

    public List<ChannelDto> findAll() {
        return channelRepository.findAll()
                .stream()
                .map(channel -> {
                    // 최신 메시지 시간 정보
                    Instant messageLastTime = messageRepository.findByChannelId(channel.getId()).stream()
                            .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                            .map(BaseEntity::getCreatedAt)
                            .findFirst().orElse(null);

                    List<UUID> userIds = readStatusRepository.findByChannelId(channel.getId()).stream()
                            .map(ReadStatus::getUserId)
                            .toList();
                    return ChannelDto.of(channel, userIds, messageLastTime);

                }).toList();
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
                            .stream().anyMatch(readStatus -> readStatus.getUserId().equals(requestDto.getId()));
                })
                .map(channel -> {
                    // 최신 메시지 시간 정보
                    Instant messageLastTime = messageRepository.findByChannelId(channel.getId()).stream()
                            .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                            .map(BaseEntity::getCreatedAt)
                            .findFirst().orElse(null);

                    List<UUID> userIds = readStatusRepository.findByChannelId(channel.getId()).stream()
                            .map(ReadStatus::getUserId)
                            .toList();
                    return ChannelDto.of(channel, userIds, messageLastTime);
                })
                .toList();
    }

    @Override
    public Channel update(ChannelIdRequestDto channelId, ChannelUpdateRequestDto requestDto) {
        Channel updateChannel = channelRepository.findById(channelId.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.CHANNEL_NOT_FOUND));

        if (updateChannel.getType().equals(ChannelType.PRIVATE)) {
            throw new GlobalCustomException(CustomStatusCode.PRIVATE_CHANNEL_CANNOT_UPDATE);
        }

        updateChannel.update(requestDto.getNewName(), requestDto.getNewDescription());
        channelRepository.update(updateChannel.getId(), updateChannel);

        return updateChannel;
    }

    @Override
    public void delete(ChannelIdRequestDto requestDto) {
        Channel deleteChannel = channelRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.CHANNEL_NOT_FOUND));

        readStatusRepository.deleteByChannelId(deleteChannel.getId());
        messageRepository.deleteByChannelId(deleteChannel.getId());
        channelRepository.delete(requestDto.getId());
    }
}
