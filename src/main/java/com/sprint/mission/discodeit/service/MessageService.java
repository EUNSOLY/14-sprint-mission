package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;

import java.util.List;

public interface MessageService {
    void save(MessageCreateRequestDto requestDto);

    MessageResponseDto find(MessageIdRequestDto requestDto);

    List<MessageResponseDto> findByUserId(UserIdRequestDto requestDto);

    List<MessageResponseDto> findByChannelIdAndUserId(UserIdRequestDto userRequestDto, ChannelIdRequestDto channelRequestDto);

    List<MessageResponseDto> findAllByChannelId(ChannelIdRequestDto requestDto);

    void update(MessageUpdateRequestDto request);

    void delete(MessageIdRequestDto requestDto);
}
