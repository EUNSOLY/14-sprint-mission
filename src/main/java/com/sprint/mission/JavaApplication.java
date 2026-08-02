package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class JavaApplication {
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
        // 레포지토리 초기화
        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        // 서비스 초기화
        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository, userService, channelService);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, user, channel);
    }
}
