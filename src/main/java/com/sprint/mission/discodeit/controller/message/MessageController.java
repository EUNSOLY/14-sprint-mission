package com.sprint.mission.discodeit.controller.message;


import com.sprint.mission.discodeit.common.dto.ApiResponse;
import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentMapper;
import com.sprint.mission.discodeit.service.message.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Message", description = "Message API")
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST, value = "/api/message")
    public ResponseEntity<ApiResponse<Void>> sendMessage(
            @RequestPart(value = "messageInfo") MessageCreateRequestDto request,
            @RequestPart(value = "contents", required = false) List<MultipartFile> contentFiles
    ) throws IOException {
        List<BinaryContentCreateRequestDto> binaryRequests = BinaryContentMapper.toList(contentFiles);
        messageService.save(request, binaryRequests);
        return ApiResponse.toSuccess(CustomStatusCode.OK, null);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/message/{id}")
    public ResponseEntity<ApiResponse<Void>> updateMessage(
            @PathVariable(value = "id") UUID messageId,
            @RequestPart(value = "messageInfo") MessageUpdateRequestDto request,
            @RequestPart(value = "contents", required = false) List<MultipartFile> contentFiles
    ) throws IOException {
        List<BinaryContentCreateRequestDto> binaryRequests = BinaryContentMapper.toList(contentFiles);
        messageService.update(request, binaryRequests);
        return ApiResponse.toSuccess(CustomStatusCode.OK, null);

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/message/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(
            @PathVariable(value = "id") UUID messageId
    ) {
        messageService.delete(MessageIdRequestDto.from(messageId));
        return ApiResponse.toSuccess(CustomStatusCode.OK, null);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/message", params = "channelId")
    public ResponseEntity<ApiResponse<List<MessageResponseDto>>> getMessagesByChannelId(
            @RequestParam("channelId") UUID channelId
    ) {
        List<MessageResponseDto> messages = messageService.findAllByChannelId(ChannelIdRequestDto.from(channelId));

        return ApiResponse.toSuccess(CustomStatusCode.OK, messages);
    }
}
