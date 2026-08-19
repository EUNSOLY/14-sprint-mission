package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;

import java.util.List;

public interface ReadStatusService {
    void save(ReadStatusCreateRequestDto request);

    ReadStatusResponseDto find(ReadStatusIdRequestDto request);

    List<ReadStatusResponseDto> findAllByUserId(UserIdRequestDto request);

    ReadStatusResponseDto update(ReadStatusUpdateRequestDto request);

    void delete(ReadStatusIdRequestDto request);
}
