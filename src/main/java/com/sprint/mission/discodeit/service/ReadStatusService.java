package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;

import java.util.List;

public interface ReadStatusService {
    void save(ReadStatusCreateRequestDto request);

    ReadStatusResponseDto find(ReadStatusIdRequestDto request);

    List<ReadStatusResponseDto> findAllByUserId(UserIdRequestDto request);

    ReadStatusResponseDto update(ReadStatusUpdateRequestDto request);

    void delete(ReadStatusIdRequestDto request);
}
