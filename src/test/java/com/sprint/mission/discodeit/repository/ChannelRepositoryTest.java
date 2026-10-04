package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.common.config.JpaConfig;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaConfig.class)
class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    private Channel publicChannel;
    private Channel privateChannel;

    @BeforeEach
    void setUp() {
        publicChannel = channelRepository.save(Channel.create(ChannelType.PUBLIC, "공개채널", "공개채널 설명"));
        privateChannel = channelRepository.save(Channel.create(ChannelType.PRIVATE, null, null));
        channelRepository.save(Channel.create(ChannelType.PUBLIC, " ", "공개채널2 설명"));
    }

    // 커스텀 쿼리나 pagination을 사용하지않아서 우선 미 작성
}
