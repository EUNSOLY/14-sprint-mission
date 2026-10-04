package com.sprint.mission.discodeit.controller.message;


import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageIdRequestDto;
import com.sprint.mission.discodeit.dto.message.data.MessageDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.message.MessageService;
import com.sprint.mission.discodeit.service.user.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MessageIntegrationTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    @DisplayName("메세지 생성")
    void create_message_success() {
        // given
        UserCreateRequest request = new UserCreateRequest("sol", "sol@test.com", "1234");
        User savedUser = userService.save(request, null);
        PublicChannelCreateRequestDto requestDto = new PublicChannelCreateRequestDto("공개채널 1", "공개채널 설명서");
        ChannelDto savedChannel = channelService.save(requestDto);
        MessageCreateRequestDto messageRequest = new MessageCreateRequestDto("메세지컨텍스트", savedChannel.id(), savedUser.getId());

        // when
        MessageDto savedMessage = messageService.save(messageRequest, null);
        entityManager.flush(); // 영속성 컨텍스트의 변경을 DB에 반영(INSERT 실행). 이때 @CreationTimestamp로 createdAt이 채워짐
        entityManager.clear(); // 영속성 컨텍스트 비움 → 이후 조회는 DB에서 새로 읽음

        // then: DB 조회해서 확인
        Message found = messageRepository.findById(savedMessage.id()).orElseThrow();
        assertThat(found.getContent()).isEqualTo("메세지컨텍스트");
        assertThat(found.getCreatedAt()).isNotNull();
    }


    @Test
    @DisplayName("메세지 삭제")
    void delete_message_success() {
        // given: 삭제할 메세지 실제로 생성
        UserCreateRequest request = new UserCreateRequest("sol", "sol@test.com", "1234");
        User savedUser = userService.save(request, null);
        PublicChannelCreateRequestDto requestDto = new PublicChannelCreateRequestDto("공개채널 1", "공개채널 설명서");
        ChannelDto savedChannel = channelService.save(requestDto);
        MessageCreateRequestDto messageRequest = new MessageCreateRequestDto("메세지컨텍스트", savedChannel.id(), savedUser.getId());
        MessageDto savedMessage = messageService.save(messageRequest, null);

        // when
        messageService.delete(MessageIdRequestDto.from(savedMessage.id()));
        entityManager.flush();
        entityManager.clear();

        // then : DB 체크
        assertThat(messageRepository.findById(savedMessage.id())).isEmpty();
    }

}
