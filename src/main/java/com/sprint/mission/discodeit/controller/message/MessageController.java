package com.sprint.mission.discodeit.controller.message;


import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentMapper;
import com.sprint.mission.discodeit.service.message.MessageService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/messages")
public class MessageController implements MessageControllerDocs {
    private final MessageService messageService;

    @Override
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> create(
            @RequestPart(value = "messageCreateRequest") MessageCreateRequestDto request,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> contentFiles
    ) throws IOException {
        List<BinaryContentCreateRequestDto> binaryRequests = BinaryContentMapper.toList(contentFiles);
        Message savedMessage = messageService.save(request, binaryRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

    @Override
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ResponseEntity<Message> updateMessage(
            @PathVariable(value = "id") UUID messageId,
            @RequestBody MessageUpdateRequestDto request
    ) throws IOException {
        messageService.update(MessageIdRequestDto.from(messageId), request);
        return ResponseEntity.status(HttpStatus.OK).body(null);

    }

    @Override
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "삭제할 Message ID")
            @PathVariable(value = "id") UUID messageId
    ) {
        messageService.delete(MessageIdRequestDto.from(messageId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getMessagesByChannelId(
            @Parameter(description = "조회할 Channel ID")
            @RequestParam("channelId") UUID channelId
    ) {
        List<Message> responses = messageService.findAllByChannelId(ChannelIdRequestDto.from(channelId));

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
