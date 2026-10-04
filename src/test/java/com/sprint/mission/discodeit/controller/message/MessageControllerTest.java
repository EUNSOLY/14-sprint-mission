package com.sprint.mission.discodeit.controller.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageIdRequestDto;
import com.sprint.mission.discodeit.dto.message.data.MessageDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.service.message.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MessageService messageService;

    @Test
    @DisplayName("메세지를 생성한다 201 반환")
    void create_message_success() throws Exception {
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MessageCreateRequestDto messageRequest = new MessageCreateRequestDto("메세지 내용", channelId, userId);
        User user = User.create("aaron", "aaron@test.com", "1234");
        Channel channel = Channel.create(ChannelType.PUBLIC, "공개채널1", "공개채널");
        Message message = Message.create("메세지 내용", user, channel);

        MockMultipartFile messageCreateRequest = new MockMultipartFile(
                "messageCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(messageRequest)
        );

        when(messageService.save(any(MessageCreateRequestDto.class), any()))
                .thenReturn(MessageDto.to(message, null, null, null));

        mockMvc.perform(multipart("/api/messages")
                        .file(messageCreateRequest)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("메세지 내용"))
        ;

        verify(messageService).save(any(), any());

    }

    @Test
    @DisplayName("메세지 삭제에 성공한다. 204 반환")
    void delete_message_success() throws Exception {

        UUID messageId = UUID.randomUUID();

        // void 반환하는 메서드 모킹
        doNothing().when(messageService).delete(MessageIdRequestDto.from(messageId));

        mockMvc.perform(delete("/api/messages/{id}", messageId)
                )
                .andDo(print())
                .andExpect(status().isNoContent())
        ;

        verify(messageService).delete(argThat(item -> item.getId().equals(messageId)));

    }


}