package com.sprint.mission.discodeit.controller.channel;


import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ChannelIntegrationTest {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private EntityManager entityManager;
    
    @Test
    @DisplayName("공개 채널 생성")
    void create_channel_success() {
        // given
        PublicChannelCreateRequestDto requestDto = new PublicChannelCreateRequestDto("공개채널 1", "공개채널 설명서");

        // when
        ChannelDto saved = channelService.save(requestDto);
        entityManager.flush(); // 영속성 컨텍스트의 변경을 DB에 반영(INSERT 실행). 이때 @CreationTimestamp로 createdAt이 채워짐
        entityManager.clear(); // 영속성 컨텍스트 비움 → 이후 조회는 DB에서 새로 읽음

        // then: DB 조회해서 확인
        Channel found = channelRepository.findById(saved.id()).orElseThrow();
        assertThat(found.getName()).isEqualTo("공개채널 1");
        assertThat(found.getDescription()).isEqualTo("공개채널 설명서");
        assertThat(found.getCreatedAt()).isNotNull();
    }


    @Test
    @DisplayName("공개 채널 삭제")
    void delete_channel_success() {
        // given: 삭제할 채널 실제로 생성
        PublicChannelCreateRequestDto requestDto = new PublicChannelCreateRequestDto("공개채널 1", "공개채널 설명서");
        ChannelDto saved = channelService.save(requestDto);
        UUID channelId = saved.id();
        entityManager.flush();
        entityManager.clear();

        // when
        channelService.delete(ChannelIdRequestDto.from(channelId));
        entityManager.flush();
        entityManager.clear();

        // then : DB 체크
        assertThat(channelRepository.findById(channelId)).isEmpty();
    }

}
