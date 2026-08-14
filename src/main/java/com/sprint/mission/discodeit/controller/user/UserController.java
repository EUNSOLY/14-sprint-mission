package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.common.utils.BinaryContentMapper;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController {
    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "")
    public void createUser(
            @RequestPart(value = "user") UserCreateRequestDto request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        BinaryContentCreateRequestDto binaryRequest = BinaryContentMapper.to(profile);
        userService.save(request, binaryRequest);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public void updateUser(
            @PathVariable(value = "id") UUID userId,
            @RequestPart(value = "user") UserUpdateRequestDto request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        BinaryContentCreateRequestDto binaryRequest = BinaryContentMapper.to(profile);
        userService.update(request, binaryRequest);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteUser(
            @PathVariable(value = "id") UUID deleteUserId
    ) {
        userService.delete(UserIdRequestDto.from(deleteUserId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public UserResponseDto getUser(
            @PathVariable(value = "id") UUID userId
    ) {
        return userService.find(UserIdRequestDto.from(userId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findAll")
    public List<UserResponseDto> findUsers(
    ) {
        return userService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<UserResponseDto> getUsers(
    ) {
        return userService.findAll();
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public void updateOnlineStatus(
            @PathVariable(value = "id") UUID userId
    ) {
        userService.updateUserOnlineStatus(UserIdRequestDto.from(userId));
    }

}
