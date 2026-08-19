package com.sprint.mission.discodeit.controller.auth;

import com.sprint.mission.discodeit.dto.auth.LoginRequestDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.service.auth.BaseAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 API")
public class AuthController {
    private final BaseAuthService authService;

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 인증한다.")
    @RequestMapping(method = RequestMethod.POST, value = "/api/auth/login")
    public ResponseEntity<User> login(
            @RequestBody LoginRequestDto request
    ) {
        User loginUser = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginUser);
    }
}
