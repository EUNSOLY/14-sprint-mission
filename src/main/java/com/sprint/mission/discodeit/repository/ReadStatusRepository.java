package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    List<ReadStatus> findByUserId(UUID userId);

    List<ReadStatus> findByChannelId(UUID id);
}
