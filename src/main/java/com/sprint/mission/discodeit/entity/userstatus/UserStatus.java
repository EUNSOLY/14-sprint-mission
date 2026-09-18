package com.sprint.mission.discodeit.entity.userstatus;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Entity
@Getter
@Table(name = "user_statuses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant lastActiveAt;

    public static UserStatus create(User user) {
        return new UserStatus(user);
    }

    private UserStatus(User user) {
        this.user = user;
        this.lastActiveAt = Instant.now();
    }


    public Instant updateLastAccessAt() {
        this.lastActiveAt = Instant.now();
//        super.updatedAt();
        return this.lastActiveAt;
    }

    // 마지막 접속 시간 기준 현재 로그인 유저 판단 메서드
    public boolean isOnline() {
        if (Objects.isNull(this.lastActiveAt)) return false;
        Instant now = Instant.now();
        Duration duration = Duration.between(this.lastActiveAt, now);
        // 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주
        return duration.getSeconds() <= 300;
    }
}
