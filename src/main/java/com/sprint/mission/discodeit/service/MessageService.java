package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;

import java.util.List;

public interface MessageService {
    void save(MessageCreateRequestDto requestDto);

    MessageRespnoseDto find(MessageIdRequestDto requestDto);

    List<MessageRespnoseDto> findByUserId(UserIdRequestDto requestDto);

    List<MessageRespnoseDto> findByChannelIdAndUserId(UserIdRequestDto userRequestDto, ChannelIdRequestDto channelRequestDto);

    List<MessageRespnoseDto> findAllByChannelId(ChannelIdRequestDto requestDto);

    void update(MessageUpdateRequestDto request);

    void delete(MessageIdRequestDto requestDto);
}
