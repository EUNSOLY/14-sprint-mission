package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

    static User setupUser(UserService userService) {
        userService.save(new User("woody", "010-0000-1111", "woody1234", UserStatus.ONLINE));
        return userService.findAll().stream().filter(user -> user.getName().equals("woody")).findFirst().orElse(null);

    }

    static Channel setupChannel(ChannelService channelService) {
        channelService.save(new Channel("채널1"));
        return channelService.findAll().stream().filter(user -> user.getName().equals("채널1")).findFirst().orElse(null);

    }

    static void messageCreateTest(MessageService messageService, User author, Channel channel) {
        System.out.println("메시지 생성");
        messageService.save(new Message("메세지1", author.getId(), channel.getId()));
    }


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
        

        // TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, user, channel);


    }

}
