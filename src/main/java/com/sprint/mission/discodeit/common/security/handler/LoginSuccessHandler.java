package com.sprint.mission.discodeit.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.service.auth.DiscodietUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        // 1. principal 에서 user 꺼내오기
        DiscodietUserDetails principal = (DiscodietUserDetails) authentication.getPrincipal();
        UserDto principalDto = principal.getUserDto();

        // principal의 UserDto는 인증 전에 만들어져 online=false 이므로, 로그인 성공 응답에서는 true로 내려준다
        UserDto userDto = new UserDto(
                principalDto.id(), principalDto.username(), principalDto.email(),
                principalDto.role(), principalDto.profile(), true
        );

        // 2. 응답
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), userDto);

    }
}
