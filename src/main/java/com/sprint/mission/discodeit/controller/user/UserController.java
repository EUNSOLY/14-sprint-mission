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
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping(value = "/api/users")
@Tag(name = "User", description = "User API")
public class UserController implements UserControllerDocs {
    private final UserService userService;

    @Override
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<User> create(
            @Parameter(description = "User 생성 정보")
            @RequestPart(value = "userCreateRequest") UserCreateRequest request,

            @Parameter(description = "User 프로필 이미지")
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        BinaryContentCreateRequestDto binaryRequest = BinaryContentMapper.to(profile);
        User savedUser = userService.save(request, binaryRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }

    @Override
    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<User> update(
            @Parameter(description = "수정할 User ID")
            @PathVariable("id") UUID userId,

            @Parameter(description = "User 수정 정보")
            @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,

            @Parameter(description = "수정할 User 프로필 이미지")
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        BinaryContentCreateRequestDto binaryRequest = BinaryContentMapper.to(profile);
        User updatedUser = userService.update(UserIdRequestDto.from(userId), userUpdateRequest, binaryRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUser);
    }

    @Override
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 User ID")
            @PathVariable("id") UUID deleteUserId
    ) {
        userService.delete(UserIdRequestDto.from(deleteUserId));
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable(value = "id") UUID userId
    ) {
        UserDto user = userService.find(UserIdRequestDto.from(userId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }
    
    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll(
    ) {
        List<UserDto> users = userService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    @Override
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}/userStatus")
    public ResponseEntity<UserStatus> updateOnlineStatus(
            @Parameter(description = "상태를 변경할 User ID")
            @PathVariable(value = "id") UUID userId
    ) {
        UserStatus userStatus = userService.updateUserOnlineStatus(UserIdRequestDto.from(userId));
        return ResponseEntity.status(HttpStatus.OK).body(userStatus);
    }
}
