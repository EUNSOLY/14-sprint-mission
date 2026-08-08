package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.ReadStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void save(ReadStatusCreateRequestDto request);

    ReadStatus find(ReadStatusIdRequestDto requestDto);

    List<ReadStatus> findAllByUserId(UserIdRequestDto requestDto);

    ReadStatus update(ReadStatusUpdateRequestDto requestDto);

    void delete(UUID id);
}
