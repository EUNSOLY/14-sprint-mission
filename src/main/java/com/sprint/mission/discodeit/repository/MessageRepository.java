package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    // Pageable을 사용하면 orderBy가 된다고해서 JPQL에서는 미사용
    @Query("""
            select m from Message m
            where m.channel.id = :channelId
            and m.createdAt < :cursor
            """)
    Slice<Message> findByChannelId(
            @Param("channelId") UUID channelId,
            @Param("cursor") Instant cursor,
            Pageable pageable
    );

    @Query("""
            select m from Message m
            where m.channel.id = :channelId
            """)
    Slice<Message> findFirstPage(
            @Param("channelId") UUID channelId,
            Pageable pageable
    );

    Page<Message> findByChannelId(UUID channelId, Pageable pageable);

    Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);

    List<Message> findByChannelIdAndAuthorId(UUID userId, UUID channelId);

}
