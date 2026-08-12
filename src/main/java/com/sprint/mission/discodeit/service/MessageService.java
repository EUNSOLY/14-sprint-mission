package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;

import java.util.List;

public interface MessageService {
    void save(
            MessageCreateRequestDto requestDto,
            List<BinaryContentCreateRequestDto> messageContentCreateRequests
    );

    MessageResponseDto find(MessageIdRequestDto requestDto);

    List<MessageResponseDto> findByUserId(UserIdRequestDto requestDto);

    List<MessageResponseDto> findByChannelIdAndUserId(UserIdRequestDto userRequestDto, ChannelIdRequestDto channelRequestDto);

    List<MessageResponseDto> findAllByChannelId(ChannelIdRequestDto requestDto);

    void update(
            MessageUpdateRequestDto request,
            List<BinaryContentCreateRequestDto> messageContentCreateRequests

    );

    void delete(MessageIdRequestDto requestDto);
}
