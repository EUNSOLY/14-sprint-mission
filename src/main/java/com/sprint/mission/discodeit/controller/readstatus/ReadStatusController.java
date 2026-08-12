package com.sprint.mission.discodeit.controller.readstatus;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.UserIdRequestDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/read-status")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST, value = "")
    public void createMessageReadStatus(
            @RequestBody ReadStatusCreateRequestDto request
    ) {
        readStatusService.save(request);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public void updateMessageReadStatus(
            @PathVariable(value = "id") UUID readStatusId,
            @RequestBody ReadStatusUpdateRequestDto request
    ) {
        readStatusService.update(request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public List<ReadStatusResponseDto> getMessageReadStatusByUserId(
            @PathVariable(value = "id") UUID userId
    ) {
        return readStatusService.findAllByUserId(UserIdRequestDto.from(userId));
    }
}
