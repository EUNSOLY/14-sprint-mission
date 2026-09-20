package com.sprint.mission.discodeit.entity.readstatus;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.user.User;
import jakarta.persistence.*;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;
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
