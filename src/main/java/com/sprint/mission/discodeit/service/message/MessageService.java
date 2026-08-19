package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;

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
