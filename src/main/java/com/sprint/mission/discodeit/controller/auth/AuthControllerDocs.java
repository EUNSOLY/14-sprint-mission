package com.sprint.mission.discodeit.controller.auth;

import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.service.auth.DiscodietUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(
            summary = "현재 로그인한 사용자 조회",
            description = "세션에 저장된 인증 정보로 현재 로그인한 사용자 정보를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "현재 사용자 조회 성공",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "로그인하지 않은 사용자",
            content = @Content
    )
    ResponseEntity<UserDto> getMe(@Parameter(hidden = true) DiscodietUserDetails userDetails);

    @Operation(
            summary = "사용자 권한 수정",
            description = "사용자의 권한(Role)을 변경합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "권한 수정 성공",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음 (사용자가 존재하지 않습니다.)",
            content = @Content
    )
    @ApiResponse(
            responseCode = "403",
            description = "권한 수정 권한이 없음",
            content = @Content
    )
    ResponseEntity<UserDto> updateUserRole(UserRoleUpdateRequest request);
}
