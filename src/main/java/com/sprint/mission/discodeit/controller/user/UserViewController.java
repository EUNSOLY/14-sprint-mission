package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserViewController {
    private final UserService userService;

    @RequestMapping(value = "/user")
    public String getUsersPage(ModelMap modelMap) {
        List<UserDto> userResponse = userService.findAll();
        modelMap.addAttribute("users", userResponse);

        return "/users/user-list";
    }
}
