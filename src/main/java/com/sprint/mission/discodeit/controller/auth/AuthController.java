package com.sprint.mission.discodeit.controller.auth;

import com.sprint.mission.discodeit.common.dto.ApiResponse;
import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.dto.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "")
    public ResponseEntity<ApiResponse<UserResponseDto>> login(
            @RequestBody LoginRequestDto request
    ) {
        User loginUser = authService.validateCredentials(request);
        UserResponseDto userResponseDto = userService.find(UserIdRequestDto.from(loginUser.getId()));
        return ApiResponse.toSuccess(CustomStatusCode.OK, userResponseDto);
    }
}
