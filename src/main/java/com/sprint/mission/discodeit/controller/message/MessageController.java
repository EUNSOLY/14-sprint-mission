package com.sprint.mission.discodeit.controller.message;


import com.sprint.mission.discodeit.common.utils.BinaryContentMapper;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            @RequestPart(value = "messageInfo") MessageCreateRequestDto request,
            @RequestPart(value = "contents", required = false) List<MultipartFile> contentFiles
    ) throws IOException {
        List<BinaryContentCreateRequestDto> binaryRequests = BinaryContentMapper.toList(contentFiles);
        messageService.save(request, binaryRequests);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public void updateMessage(
            @PathVariable(value = "id") UUID messageId,
            @RequestPart(value = "messageInfo") MessageUpdateRequestDto request,
            @RequestPart(value = "contents", required = false) List<MultipartFile> contentFiles
    ) throws IOException {
        List<BinaryContentCreateRequestDto> binaryRequests = BinaryContentMapper.toList(contentFiles);
        messageService.update(request, binaryRequests);
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
