package com.sprint.mission.discodeit.entity.readstatus;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Entity
@Table(name = "read_statuses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus extends BaseUpdatableEntity {
    // 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", columnDefinition = "uuid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", columnDefinition = "uuid")
    private Channel channel;
    
    @Column(columnDefinition = "timestamp with time zone", nullable = false)
    private Instant lastReadAt;

    public static ReadStatus create(User user, Channel channel) {
        return new ReadStatus(user, channel);
    }

    private ReadStatus(
            User user,
            Channel channel
    ) {
        this.user = user;
        this.channel = channel;
        this.lastReadAt = Instant.now();
    }

    public Instant updateLastReadMessageAt() {
        this.lastReadAt = Instant.now();
        return this.lastReadAt;
    }
}
