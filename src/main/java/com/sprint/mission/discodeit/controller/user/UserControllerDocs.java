package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserControllerDocs {

    @Operation(summary = "User 등록")
    @ApiResponse(
            responseCode = "201",
            description = "User가 성공적으로 생성됨",
            content = @Content(schema = @Schema(implementation = User.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
            content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject("User with email {email} already exists"))
    )
    @RequestBody(content = @Content(encoding = @Encoding(name = "userCreateRequest", contentType = MediaType.APPLICATION_JSON_VALUE)))
    ResponseEntity<User> create(
            @Parameter(description = "User 생성 정보")
            UserCreateRequest request,

            @Parameter(description = "User 프로필 이미지")
            MultipartFile profile
    ) throws IOException;

    @Operation(summary = "User 정보 수정")
    @ApiResponse(
            responseCode = "404",
            description = "User를 찾을 수 없음",
            content = @Content(schema = @Schema(examples = "User with id {userId} not found"))
    )
    @ApiResponse(
            responseCode = "400",
            description = "같은 email 또는 username을 사용하는 User가 이미 존재함",
            content = @Content(schema = @Schema(examples = "user with email {newEmail} already exists"))
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 정보가 성공적으로 수정됨",
            content = @Content(schema = @Schema(implementation = User.class))
    )
    ResponseEntity<User> update(
            @Parameter(description = "수정할 User ID")
            UUID userId,

            @Parameter(description = "User 수정 정보")
            UserUpdateRequest userUpdateRequest,

            @Parameter(description = "수정할 User 프로필 이미지")
            MultipartFile profile
    ) throws IOException;

    @Operation(summary = "User 삭제")
    @ApiResponse(
            responseCode = "204",
            description = "User가 성공적으로 삭제됨")
    @ApiResponse(
            responseCode = "404",
            description = "User를 찾을 수 없음",
            content = @Content(examples = @ExampleObject("User with id {id} not found"))
    )
    ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 User ID")
            UUID deleteUserId
    );

    @Operation(summary = "User 단일 조회")
    @ApiResponse(
            responseCode = "200",
            description = "User가 성공적으로 조회됨",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "User를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject("User with id {id} not found"))
    )
    ResponseEntity<UserDto> getUser(
            @Parameter(description = "조회할 User ID")
            UUID userId
    );

    @Operation(summary = "전체 User 목록 조회")
    @ApiResponse(
            responseCode = "200",
            description = "User 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
    )
    ResponseEntity<List<UserDto>> findAll();

    @Operation(summary = "User 온라인 상태 업데이트")
    @ApiResponse(
            responseCode = "404",
            description = "해당 User의 UserStatus를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = String.class, examples = "UserStatus with userId {userId} not found"))
    )
    @ApiResponse(
            responseCode = "200",
            description = "User 온라인 상태가 성공적으로 업데이트됨",
            content = @Content(schema = @Schema(implementation = UserStatus.class))
    )
    ResponseEntity<UserStatus> updateOnlineStatus(
            @Parameter(description = "상태를 변경할 User ID")
            UUID userId
    );
}
