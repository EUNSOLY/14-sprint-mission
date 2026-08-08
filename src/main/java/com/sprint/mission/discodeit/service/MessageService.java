package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageService {
    void save(MessageCreateRequestDto requestDto);

    Message find(MessageIdRequestDto requestDto);

    List<Message> findByUserId(UserIdRequestDto requestDto);

    List<Message> findByChannelIdAndUserId(UserIdRequestDto userRequestDto, ChannelIdRequestDto channelRequestDto);

    List<Message> findAllByChannelId(ChannelIdRequestDto requestDto);

    void update(MessageUpdateRequestDto request);

    void delete(MessageIdRequestDto requestDto);
}
