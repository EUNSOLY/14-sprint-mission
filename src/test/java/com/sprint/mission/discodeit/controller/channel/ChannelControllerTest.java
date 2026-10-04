package com.sprint.mission.discodeit.controller.channel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ChannelController.class)
class ChannelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ChannelService channelService;

    @Test
    @DisplayName("공개 채널 생성 성공")
    void create_private_channel_success() throws Exception {
        // given - 요청 데이터, 응답설정
        UUID channelId = UUID.randomUUID();
        PublicChannelCreateRequestDto request = new PublicChannelCreateRequestDto("공개채널1", "공개채널");
        Channel channel = Channel.create(ChannelType.PUBLIC, "공개채널1", "공개채널");
        ReflectionTestUtils.setField(channel, "id", channelId);

        // when
        when(channelService.save(any(PublicChannelCreateRequestDto.class))).thenReturn(ChannelDto.of(channel, null, null));

        mockMvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("공개채널1"));


        // then
        verify(channelService).save(any(PublicChannelCreateRequestDto.class));
    }

    @Test
    @DisplayName("접근 가능한 채널만 조회 성공")
    void get_accessible_channels_success() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        User user = User.create("ss", "ss@test.com", "134");
        UserDto userDto = UserDto.of(user, null, false);
        Channel publicChannel = Channel.create(ChannelType.PUBLIC, "공개채널1", "공개채널!!");
        Channel privateChannel = Channel.create(ChannelType.PRIVATE, null, null);

        List<ChannelDto> channels = List.of(
                ChannelDto.of(publicChannel, null, Instant.now()),
                ChannelDto.of(privateChannel, List.of(userDto), Instant.now())
        );

        // when
        when(channelService.findAllByUserId(argThat(dto -> dto.getId().equals(userId))))
                .thenReturn(channels);

        mockMvc.perform(get("/api/channels")
                        .param("userId", String.valueOf(userId))
                )
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].type").value("PUBLIC"))
                .andExpect(jsonPath("$[0].name").value("공개채널1"))
                .andExpect(jsonPath("$[1].type").value("PRIVATE"))
        ;

        // then
        verify(channelService).findAllByUserId(any());
    }
}