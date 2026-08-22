package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;

import java.util.List;

public interface ReadStatusService {
    ReadStatus save(ReadStatusCreateRequestDto request);

    ReadStatus find(ReadStatusIdRequestDto request);

    List<ReadStatus> findAllByUserId(UserIdRequestDto request);

    ReadStatus update(ReadStatusIdRequestDto requestIdDto);

    void delete(ReadStatusIdRequestDto request);
}
