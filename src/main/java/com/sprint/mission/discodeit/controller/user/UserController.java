package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.common.dto.ApiResponse;
import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentMapper;
import com.sprint.mission.discodeit.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @RequestMapping(method = RequestMethod.POST, value = "/api/users")
    public ResponseEntity<ApiResponse<Void>> createUser(
            @RequestPart(value = "user") UserCreateRequestDto request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        BinaryContentCreateRequestDto binaryRequest = BinaryContentMapper.to(profile);
        userService.save(request, binaryRequest);

        return ApiResponse.toSuccess(CustomStatusCode.CREATED, null);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/api/users/{id}")
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @PathVariable(value = "id") UUID userId,
            @RequestPart(value = "user") UserUpdateRequestDto request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        BinaryContentCreateRequestDto binaryRequest = BinaryContentMapper.to(profile);
        userService.update(request, binaryRequest);
        return ApiResponse.toSuccess(CustomStatusCode.OK, null);

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable(value = "id") UUID deleteUserId
    ) {
        userService.delete(UserIdRequestDto.from(deleteUserId));
        return ApiResponse.toSuccess(CustomStatusCode.OK, null);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/users/{id}")

    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(
            @PathVariable(value = "id") UUID userId
    ) {
        UserResponseDto userResponseDto = userService.find(UserIdRequestDto.from(userId));
        return ApiResponse.toSuccess(CustomStatusCode.OK, userResponseDto);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/users")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getUsers(
    ) {
        List<UserResponseDto> users = userService.findAll();
        return ApiResponse.toSuccess(CustomStatusCode.OK, users);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/users/{id}")
    public ResponseEntity<ApiResponse<Void>> updateOnlineStatus(
            @PathVariable(value = "id") UUID userId
    ) {
        userService.updateUserOnlineStatus(UserIdRequestDto.from(userId));
        return ApiResponse.toSuccess(CustomStatusCode.OK, null);
    }

}
