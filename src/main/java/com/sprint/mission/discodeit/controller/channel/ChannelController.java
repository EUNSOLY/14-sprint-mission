package com.sprint.mission.discodeit.controller.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/channels")
public class ChannelController implements ChannelControllerDocs {
    private final ChannelService channelService;

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ResponseEntity<ChannelDto> createPublicChannel(
            @RequestBody @Valid PublicChannelCreateRequestDto request
    ) {
        log.info("공개 채널 생성 진입 채널명 : {}", request.name());
        ChannelDto response = channelService.save(request);
        return ResponseEntity
                .status(HttpStatus.CREATED).body(response);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/private")
    public ResponseEntity<ChannelDto> createPrivateChannel(
            @RequestBody @Valid PrivateChannelCreateRequestDto request
    ) {
        log.info("비공개 채널 생성 진입 비공개 채널 사용 유저 : {}", request.participantIds());
        ChannelDto response = channelService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ResponseEntity<ChannelDto> updatePublicChannel(
            @PathVariable(value = "id") UUID channelId,
            @RequestBody @Valid ChannelUpdateRequestDto request
    ) {
        log.info("채널 수정 - 수정 채널 : {}", channelId);
        ChannelDto channel = channelService.update(ChannelIdRequestDto.from(channelId), request);
        return ResponseEntity.status(HttpStatus.OK).body(channel);
    }

    @Override
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteChannel(
            @PathVariable(value = "id") UUID channelId
    ) {
        log.info("채널 삭제 - 삭제 채널 : {}", channelId);
        channelService.delete(ChannelIdRequestDto.from(channelId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> findAccessibleChannelsByUserId(
            @RequestParam("userId") UUID userId
    ) {
        List<ChannelDto> response = channelService.findAllByUserId(UserIdRequestDto.from(userId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
