package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserController {
    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "")
    public void createUser(
            @RequestBody UserCreateRequestDto request
    ) {
        userService.save(request);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public void updateUser(
            @PathVariable(value = "id") UUID userId,
            @RequestBody UserUpdateRequestDto request
    ) {
        userService.update(request);
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
