package com.sprint.mission.discodeit.entity.message;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", columnDefinition = "uuid")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", nullable = false, columnDefinition = "uuid")
    private Channel channel;

    @OneToMany
    @JoinTable(
            name = "message_attachments", // 별도 테이블 매핑
            joinColumns = @JoinColumn(name = "message_id", nullable = false, columnDefinition = "uuid"), // 1쪽
            inverseJoinColumns = @JoinColumn(name = "attachment_id", nullable = false, columnDefinition = "uuid")// N쪽
    )
    private List<BinaryContent> attachments = new ArrayList<>();

    public static Message create(String message, User user, Channel channel) {
        return new Message(message, user, channel);
    }

    private Message(String message, User user, Channel channel) {
        super();
        this.content = message;
        this.author = user;
        this.channel = channel;
    }

    public void update(String message) {
        if (message != null) {
            this.content = message;
        }
    }

    public void addAttachments(List<BinaryContent> attachment) {
        this.attachments.addAll(attachment);
    }


    @Override
    public String toString() {
        return String.format("Message ( \n" +
                        " id=%s, createdAt=%s, updateAt=%s \n" +
                        " name=%s, author=%s, channel=%s \n" + ")",
                super.getId(), super.getCreatedAt(), super.getUpdatedAt(),
                this.content, this.author, this.channel
        );
    }
}