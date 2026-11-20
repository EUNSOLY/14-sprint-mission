package com.sprint.mission.discodeit.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;

@Tag(name = "Auth", description = "인증 API")
public interface AuthControllerDocs {

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
