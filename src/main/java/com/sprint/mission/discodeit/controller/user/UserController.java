package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentMapper;
import com.sprint.mission.discodeit.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {
    private final UserService userService;

    // User 등록 📃
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
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/users",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<User> create(
            @Parameter(description = "User 생성 정보")
            @RequestPart(value = "userCreateRequest") UserCreateRequest request,

            @Parameter(description = "User 프로필 이미지")
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        BinaryContentCreateRequestDto binaryRequest = BinaryContentMapper.to(profile);
        userService.save(request, binaryRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }


    // User 수정 📃
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
    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/api/users/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<User> update(
            @Parameter(description = "수정할 User ID")
            @PathVariable("id") UserIdRequestDto userId,

            @Parameter(description = "User 수정 정보")
            @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,

            @Parameter(description = "수정할 User 프로필 이미지")
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        BinaryContentCreateRequestDto binaryRequest = BinaryContentMapper.to(profile);
        User updatedUser = userService.update(userId, userUpdateRequest, binaryRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUser);
    }

    // User 삭제 📃
    @Operation(summary = "User 삭제")
    @ApiResponse(
            responseCode = "204",
            description = "User가 성공적으로 삭제됨")
    @ApiResponse(
            responseCode = "404",
            description = "User를 찾을 수 없음",
            content = @Content(examples = @ExampleObject("User with id {id} not found"))
    )
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/users/{id}")
    public ResponseEntity<UserIdRequestDto> delete(
            @Parameter(description = "삭제할 User ID")
            @PathVariable("id") UUID deleteUserId
    ) {
        userService.delete(UserIdRequestDto.from(deleteUserId));
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    // User 단일 조회
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
    @RequestMapping(method = RequestMethod.GET, value = "/api/users/{id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable(value = "id") UUID userId
    ) {
        UserDto user = userService.find(UserIdRequestDto.from(userId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }


    // User 전체 조회 📃
    @Operation(summary = "전체 User 목록 조회")
    @ApiResponse(
            responseCode = "200",
            description = "User 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @RequestMapping(method = RequestMethod.GET, value = "/api/users")
    public ResponseEntity<List<UserDto>> findAll(
    ) {
        List<UserDto> users = userService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    // User 상태 업데이트 📃
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
    @RequestMapping(method = RequestMethod.PATCH, value = "/api/users/{id}/userStatus")
    public ResponseEntity<UserStatus> updateOnlineStatus(
            @PathVariable(value = "userId") UUID userId
    ) {
        UserStatus userStatus = userService.updateUserOnlineStatus(UserIdRequestDto.from(userId));
        return ResponseEntity.status(HttpStatus.OK).body(userStatus);
    }
}
