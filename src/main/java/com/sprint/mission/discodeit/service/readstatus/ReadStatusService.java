package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;

import java.util.List;

public interface ReadStatusService {
    ReadStatusDto save(ReadStatusCreateRequestDto request);

    ReadStatusDto find(ReadStatusIdRequestDto request);

    List<ReadStatusDto> findAllByUserId(UserIdRequestDto request);

    ReadStatusDto update(ReadStatusIdRequestDto requestIdDto);

    void delete(ReadStatusIdRequestDto request);
}
