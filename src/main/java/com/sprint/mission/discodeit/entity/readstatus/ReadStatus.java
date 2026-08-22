package com.sprint.mission.discodeit.entity.readstatus;

import com.sprint.mission.discodeit.entity.common.BaseEntity;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity {
    // 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(
            UUID userId,
            UUID channelId
    ) {
        this.userId = userId;
        this.channelId = channelId;
    }

    public Instant updateLastReadMessageAt() {
        this.lastReadAt = Instant.now();
        super.updatedAt();
        return this.lastReadAt;
    }

}
