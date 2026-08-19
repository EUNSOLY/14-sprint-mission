package com.sprint.mission.discodeit.controller.readstatus;

import com.sprint.mission.discodeit.common.dto.ApiResponse;
import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.service.readstatus.ReadStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST, value = "/api/read-status")
    public ResponseEntity<ApiResponse<Void>> createMessageReadStatus(
            @RequestBody ReadStatusCreateRequestDto request
    ) {
        readStatusService.save(request);
        return ApiResponse.toSuccess(CustomStatusCode.OK, null);

    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/read-status/{id}")
    public ResponseEntity<ApiResponse<Void>> updateMessageReadStatus(
            @PathVariable(value = "id") UUID readStatusId,
            @RequestBody ReadStatusUpdateRequestDto request
    ) {
        readStatusService.update(request);
        return ApiResponse.toSuccess(CustomStatusCode.OK, null);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/read-status/{id}")
    public ResponseEntity<ApiResponse<List<ReadStatusResponseDto>>> getMessageReadStatusByUserId(
            @PathVariable(value = "id") UUID userId
    ) {
        List<ReadStatusResponseDto> readStatuss = readStatusService.findAllByUserId(UserIdRequestDto.from(userId));
        return ApiResponse.toSuccess(CustomStatusCode.OK, readStatuss);
    }
}
