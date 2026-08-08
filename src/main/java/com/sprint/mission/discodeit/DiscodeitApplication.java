package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {

    static UserResponseDto setupUser(UserService userService) {
        UserCreateRequestDto requestDto = new UserCreateRequestDto("woody", "woody@codeit.com", "woody1234", null);
        userService.save(requestDto);
        return userService.findAll().stream().filter(user -> user.getName().equals("woody"))
                .findFirst()
                .orElse(null);
    }

    static ChannelResponseDto setupChannel(ChannelService channelService) {
        PublicChannelCreateRequestDto requestDto = new PublicChannelCreateRequestDto("공지", "공지 채널입니다.");
        return channelService.savePublicChannel(requestDto);
    }

    static void messageCreateTest(MessageService messageService, UserResponseDto author, ChannelResponseDto channel) {
        MessageCreateRequestDto requestDto = new MessageCreateRequestDto("메세지1", author.getId(), channel.getId(), List.of());
        System.out.println("메시지 생성");
        messageService.save(requestDto);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);


        // TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 셋업
        UserResponseDto user = setupUser(userService);
        ChannelResponseDto channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, user, channel);

//        UserIdRequestDto requestDto = new UserIdRequestDto(user.getId());
//        userService.delete(requestDto);
    }

}
