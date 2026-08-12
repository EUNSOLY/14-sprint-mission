package com.sprint.mission.discodeit.controller.message;


import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/message")
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST, value = "")
    public void sendMessage(
            @RequestBody MessageCreateRequestDto request
    ) {
        messageService.save(request);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public void updateMessage(
            @PathVariable(value = "id") UUID messageId,
            @RequestBody MessageUpdateRequestDto request
    ) {
        messageService.update(request);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteMessage(
            @PathVariable(value = "id") UUID messageId
    ) {
        messageService.delete(MessageIdRequestDto.from(messageId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public List<MessageResponseDto> getMessagesByChannelId(
            @PathVariable(value = "id") UUID channelId
    ) {
        return messageService.findAllByChannelId(ChannelIdRequestDto.from(channelId));
    }
}
