package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.readstatus.data.ReadStatusDto;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadStatusMapper {

    public ReadStatusDto toDto(ReadStatus readStatus) {
        return ReadStatusDto.to(readStatus, readStatus.getUser().getId(), readStatus.getChannel().getId());
    }
}
