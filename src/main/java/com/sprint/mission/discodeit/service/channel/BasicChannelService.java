package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserValidator userValidator;
    private final ChannelMapper channelMapper;


    @Override
    public ChannelDto save(PublicChannelCreateRequestDto request) {
        Channel savedChannel = channelRepository.save(request.toEntity());
        Instant messageLastTime = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(savedChannel.getId())
                .map(BaseEntity::getCreatedAt)
                .orElse(null);
        log.info("공개 채널 생성 완료");
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
            log.info("비공개 채널 사용자별 상태 관리 생성 - user : {}", userId);
            System.out.println(userId);
            User user = userValidator.getOrThrow(userId);
            ReadStatus readStatus = ReadStatus.create(user, savedEntity);
            readStatusRepository.save(readStatus);
        });

        log.info("비공개 채널 생성 완료");
        return channelMapper.toDto(savedEntity);
    }

    @Override
    public ChannelDto find(ChannelIdRequestDto requestDto) {
        return channelRepository.findById(requestDto.getId())
                .map(channelMapper::toDto)
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.CHANNEL_NOT_FOUND));


    }

    public List<ChannelDto> findAll() {
        return channelRepository.findAll()
                .stream()
                .map(channelMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
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
                .map(channelMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ChannelDto update(ChannelIdRequestDto channelId, ChannelUpdateRequestDto requestDto) {
        Channel updateChannel = channelRepository.findById(channelId.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.CHANNEL_NOT_FOUND));

        if (updateChannel.getType().equals(ChannelType.PRIVATE)) {
            log.warn("비공개 채널은 수정이 불가능합니다. 수정 불가 채널 : {}", channelId);
            throw new GlobalCustomException(CustomStatusCode.PRIVATE_CHANNEL_CANNOT_UPDATE);
        }

        updateChannel.update(requestDto.getNewName(), requestDto.getNewDescription());
        log.info("채널 수정 완료");
        return channelMapper.toDto(updateChannel);
    }

    @Override
    @Transactional
    public void delete(ChannelIdRequestDto requestDto) {
        Channel deleteChannel = channelRepository.findById(requestDto.getId())
                .orElseThrow(() -> {
                    log.warn("삭제할 채널 없음 삭제 채널 {}", requestDto.getId());
                    return new GlobalCustomException(CustomStatusCode.CHANNEL_NOT_FOUND);
                });

        log.info("채널 삭제 완료");
        channelRepository.delete(deleteChannel);
    }
}
