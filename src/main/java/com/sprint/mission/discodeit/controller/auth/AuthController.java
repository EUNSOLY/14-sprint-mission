package com.sprint.mission.discodeit.controller.auth;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.service.auth.BaseAuthService;
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
    private final BaseAuthService authService;

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/api/auth/login")
    public ResponseEntity<User> login(
            @RequestBody LoginRequest request
    ) {
        User loginUser = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginUser);
    }
}
