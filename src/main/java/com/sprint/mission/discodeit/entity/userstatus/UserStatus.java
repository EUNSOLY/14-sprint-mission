package com.sprint.mission.discodeit.entity.userstatus;

import com.sprint.mission.discodeit.entity.common.BaseEntity;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity {
    // 사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델
    private final UUID userId;
    private Instant lastAccessAt;

    public UserStatus(UUID userId) {
        this.userId = userId;
        this.lastAccessAt = Instant.now();
    }

    public Instant updateLastAccessAt() {
        this.lastAccessAt = Instant.now();
        super.updatedAt();
        return this.lastAccessAt;
    }

    // 마지막 접속 시간 기준 현재 로그인 유저 판단 메서드
    public boolean isCurrentlyLoggedIn() {
        if (Objects.isNull(this.lastAccessAt)) return false;
        Instant now = Instant.now();
        Duration duration = Duration.between(this.lastAccessAt, now);
        // 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주
        return duration.getSeconds() <= 300;
    }
}
