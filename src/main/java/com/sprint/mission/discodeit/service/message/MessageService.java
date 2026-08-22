package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.message.Message;

import java.util.List;

public interface MessageService {
    Message save(
            MessageCreateRequestDto requestDto,
            List<BinaryContentCreateRequestDto> messageContentCreateRequests
    );

    Message find(MessageIdRequestDto requestDto);

    List<Message> findByUserId(UserIdRequestDto requestDto);

    List<Message> findByChannelIdAndUserId(UserIdRequestDto userRequestDto, ChannelIdRequestDto channelRequestDto);

    List<Message> findAllByChannelId(ChannelIdRequestDto requestDto);

    Message update(
            MessageIdRequestDto messageIdRequest,
            MessageUpdateRequestDto request
    );

    void delete(MessageIdRequestDto requestDto);
}
