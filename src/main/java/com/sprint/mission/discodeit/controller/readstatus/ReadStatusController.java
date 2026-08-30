package com.sprint.mission.discodeit.controller.readstatus;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.service.readstatus.ReadStatusService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
@RequestMapping(value = "/api/readStatuses")
public class ReadStatusController implements ReadStatusControllerDocs {
    private final ReadStatusService readStatusService;


    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatus> createMessageReadStatus(
            @RequestBody ReadStatusCreateRequestDto request
    ) {
        ReadStatus readStatus = readStatusService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);

    }

    @Override
    @RequestMapping(method = RequestMethod.PATCH, value = "/{readStatusId}")
    public ResponseEntity<ReadStatus> updateMessageReadStatus(
            @Parameter(description = "수정할 읽음 상태 ID")
            @PathVariable(value = "readStatusId") UUID readStatusId,

            @RequestBody ReadStatusUpdateRequestDto requestDto
    ) {
        readStatusService.update(ReadStatusIdRequestDto.from(readStatusId));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> findAllByUserId(
            @Parameter(description = "조회할 User ID")
            @RequestParam(value = "userId") UUID userId
    ) {
        List<ReadStatus> readStatusResponse = readStatusService.findAllByUserId(UserIdRequestDto.from(userId));
        return ResponseEntity.status(HttpStatus.OK).body(readStatusResponse);
    }

}
