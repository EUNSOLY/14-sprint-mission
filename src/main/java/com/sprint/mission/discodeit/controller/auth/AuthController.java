package com.sprint.mission.discodeit.controller.auth;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.service.auth.AuthService;
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
public class AuthController implements AuthControllerDocs {
    private final AuthService authService;

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/api/auth/login")
    public ResponseEntity<UserDto> login(
            @RequestBody LoginRequest request
    ) {
        UserDto loginUser = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginUser);
    }
}
