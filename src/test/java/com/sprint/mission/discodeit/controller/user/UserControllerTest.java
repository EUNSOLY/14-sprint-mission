package com.sprint.mission.discodeit.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
//@Import(SecurityConfig.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;            // 서버의 MVC 동작을 테스트가능하게 함
    @Autowired
    private ObjectMapper objectMapper;  // JSON으로 파싱하기 위해 주입

    @MockitoBean                        // 서비스로직의 테스트 아니니까 가짜 객체 주입
    private UserService userService;

    @Test
    @DisplayName("사진없이 회원 이름, 이메일, 비밀번호를 통해 회원을 생성한다.")
    void create_member_with_outPhoto() throws Exception {
        // Given - 요청 데이터, 응답 설정
        UserCreateRequest userRequest = new UserCreateRequest("aaron", "aaron@test.com", "1234");
        User user = User.create("aaron", "aaron@test.com", "1234");
        MockMultipartFile userCreateRequest = new MockMultipartFile(
                "userCreateRequest",              // 파트 이름
                "",                                     // 파일명 (JSON이라 비워도 됨)
                MediaType.APPLICATION_JSON_VALUE,       // 이 파트의 Content-Type
                objectMapper.writeValueAsBytes(userRequest)
        );
        when(userService.save(any(UserCreateRequest.class), any()))
                .thenReturn(user);

        // When & Then - POST 요청 처리 검증
        mockMvc.perform(
                        multipart("/api/users")
                                .file(userCreateRequest)
                )
                .andExpect(status().isCreated()) // 응답의 예상값을 작성하여 검증한다.
                .andExpect(jsonPath("$.username").value("aaron")) //  응답 필드 항목의 존재여부(exist())나 값(value())의 예상값을 정할 수 있다
                .andDo(print()); // 용청과 응답 정보를 모두 출력한다.
        ;

        verify(userService).save(any(UserCreateRequest.class), any());
    }


    @Test
    @DisplayName("사용자 삭제 성공 - 204 OK 반환")
    void delete_userById_success() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();

        // void를 반환하는 서비스 메서드 동작 모킹
        doNothing().when(userService).delete(UserIdRequestDto.from(userId));

        // when & then
        mockMvc.perform(delete("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isNoContent());

        // 실제 호출 검증
        verify(userService).delete(argThat(dto -> dto.getId().equals(userId)));
    }

    @Test
    @DisplayName("사용자 삭제 실패 - 존재하지 않는 ID인 경우 404 반환")
    void delete_UserById_Failure() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();

        doThrow(new UserNotFoundException(Map.of("사용자 ID", userId)))
                .when(userService).delete(UserIdRequestDto.from(any()));

        // when & then
        mockMvc.perform(delete("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNotFound());

        // 실제 호출 검증
        verify(userService).delete(argThat(dto -> dto.getId().equals(userId)));
    }
}