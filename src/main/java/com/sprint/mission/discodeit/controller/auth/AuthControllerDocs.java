package com.sprint.mission.discodeit.controller.auth;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;

@Tag(name = "Auth", description = "인증 API")
public interface AuthControllerDocs {
    @Operation(summary = "로그인")
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(schema = @Schema(implementation = User.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음",
            content = @Content(examples = @ExampleObject("User with username {username} not found"))
    )
    @ApiResponse(
            responseCode = "400",
            description = "비밀번호가 일치하지 않음",
            content = @Content(examples = @ExampleObject("Wrong password"))
    )
    ResponseEntity<UserDto> login(LoginRequest request);


    @Operation(
            summary = "CSRF 토큰 발급",
            description = "XSRF-TOKEN 쿠키로 CSRF 토큰을 발급합니다. 이후 요청 시 쿠키 값을 X-XSRF-TOKEN 헤더에 담아 보내야 합니다."
    )
    @ApiResponse(
            responseCode = "203",
            description = "CSRF 토큰 발급 성공 (토큰은 XSRF-TOKEN 쿠키로 전달)",
            content = @Content
    )
    ResponseEntity<Void> getCsrfToken(@Parameter(hidden = true) CsrfToken csrfToken);
}
