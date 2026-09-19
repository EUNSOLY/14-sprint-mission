package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.controller.common.PageResponse;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.message.data.MessageDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    MessageDto save(
            MessageCreateRequestDto requestDto,
            List<BinaryContentCreateRequestDto> messageContentCreateRequests
    );

    MessageDto find(MessageIdRequestDto requestDto);

    PageResponse<List<MessageDto>> findAllByChannelId(ChannelIdRequestDto requestDto, Pageable pageable);

    MessageDto update(
            MessageIdRequestDto messageIdRequest,
            MessageUpdateRequestDto request
    );

    void delete(MessageIdRequestDto requestDto);
}
