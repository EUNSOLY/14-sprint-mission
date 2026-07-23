package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class JavaFileIOApplication {
    static User setupUser(UserService userService) {
        userService.save(new User("woody", "010-0000-1111", "woody1234", UserStatus.ONLINE));
        return userService.findAll().stream().filter(user -> user.getName() == "woody").findFirst().orElse(null);

    }

    static Channel setupChannel(ChannelService channelService) {
        channelService.save(new Channel("채널1"));
        return channelService.findAll().stream().filter(user -> user.getName() == "채널1").findFirst().orElse(null);
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        System.out.println("메시지 생성");
        messageService.save(new Message("메세지1", author.getId(), channel.getId()));
    }

    public static void main(String[] args) {
        // 서비스 초기화
        // TODO Basic*Service 구현체를 초기화하세요.
        UserService userService = new BasicUserService(new JCFUserRepository());
        ChannelService channelService = new BasicChannelService(new JCFChannelRepository());
        MessageService messageService = new BasicMessageService(new JCFMessageRepository(), userService, channelService);
//        UserService userService = new JCFUserService();
//        ChannelService channelService = new JCFChannelService();
//        MessageService messageService = new JCFMessageService(userService, channelService);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
//        // 테스트
        messageCreateTest(messageService, channel, user);

    }
}
