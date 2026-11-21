package com.sprint.mission.discodeit.controller.auth;

import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.service.auth.DiscodietUserDetails;
import com.sprint.mission.discodeit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {
    private final UserService userService;

    @Override
    @GetMapping("/api/auth/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청: {}", tokenValue);

        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
                .build();
    }

    @GetMapping("/api/auth/me")
    public ResponseEntity<UserDto> getMe(
            @AuthenticationPrincipal DiscodietUserDetails userDetails
    ) {
        UserDto userDto = userService.find(UserIdRequestDto.from(userDetails.getUserDto().id()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }

    @PutMapping("/api/auth/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUserRole(
            @RequestBody UserRoleUpdateRequest request
    ) {
        UserDto userDto = userService.updateUserRole(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }

}
