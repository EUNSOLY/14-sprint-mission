package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.common.config.JpaConfig;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaConfig.class)
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    private User user;
    private Channel channel;
    private Message message;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.create("alice", "alice@test.com", "password1"));
        channel = channelRepository.save(Channel.create(ChannelType.PUBLIC, "공개채널", "공개채널 설명"));
        Channel otherChannel = channelRepository.save(Channel.create(ChannelType.PUBLIC, "다른채널", "다른채널 설명"));

        message = messageRepository.saveAndFlush(Message.create("메세지", user, channel));
        messageRepository.saveAndFlush(Message.create("다른채널 메세지", user, otherChannel));
    }

    @Test
    @DisplayName("채널의 첫 페이지 조회 시 해당 채널의 메세지만 반환한다.")
    void findFirstPage_Success() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

        Slice<Message> result = messageRepository.findFirstPage(channel.getId(), pageable);

        assertThat(result.getContent()).extracting(Message::getContent).containsExactly("메세지");
        assertThat(result.hasNext()).isFalse();
    }

    // ========================= findByChannelId (cursor) =========================
    @Test
    @DisplayName("채널에 속한 메세지를 조회한다.")
    void findByChannelIdWithCursor_Success() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));
        Instant cursor = message.getCreatedAt().plusSeconds(1);

        Slice<Message> result = messageRepository.findByChannelId(channel.getId(), cursor, pageable);

        assertThat(result.getContent()).extracting(Message::getContent).containsExactly("메세지");
    }


    // ========================= findTopByChannelIdOrderByCreatedAtDesc =========================
    @Test
    @DisplayName("채널의 가장 최근 메세지를 조회한다.")
    void findTopByChannelId_Success() throws InterruptedException {
        messageRepository.saveAndFlush(Message.create("최근 메세지", user, channel));

        Optional<Message> result = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channel.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getContent()).isEqualTo("최근 메세지");
    }

}
