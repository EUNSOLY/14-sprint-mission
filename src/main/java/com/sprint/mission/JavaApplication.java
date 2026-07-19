package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {

        // User
        System.out.println("=============== 사용자 ===============");
        User aaron = new User("Aaron");
        User baron = new User("Baron");
        User caron = new User("Caron");

        UserService userService = new JCFUserService();
        userService.save(aaron);
        userService.save(baron);
        userService.save(caron);

        System.out.println("단건 조회");
        System.out.println(userService.find(aaron.getId()));
        System.out.println("다건 조회");
        System.out.println(userService.findAll());
        User newAaron = userService.find(aaron.getId());
        newAaron.changeName("Aaron_2");
        System.out.println(userService.find(newAaron.getId()));
        User deleteUser = userService.find(caron.getId());
        userService.delete(deleteUser.getId());

        System.out.println("최종 조회");
        System.out.println(userService.findAll());


        // Channel
        System.out.println("=============== 채널 ===============");
        Channel channel1 = new Channel("코드_공유");
        Channel channel2 = new Channel("모각코");
        Channel channel3 = new Channel("소통");

        ChannelService channelService = new JCFChannelService();
        channelService.save(channel1);
        channelService.save(channel2);
        channelService.save(channel3);
        System.out.println("단건 조회");
        System.out.println(channelService.find(channel1.getId()));
        System.out.println("다건 조회");
        System.out.println(channelService.findAll());
        Channel newChannel1 = channelService.find(channel1.getId());
        newChannel1.changeName("new_Channel1");
        System.out.println(channelService.find(newChannel1.getId()));
        Channel deleteChannel = channelService.find(channel3.getId());
        channelService.delete(deleteChannel.getId());

        System.out.println("최종 조회");
        System.out.println(channelService.findAll());

        System.out.println("=============== 메세지 ===============");
        Message message1 = new Message("첫번째 메세지 입니다.", baron.getId(), channel2.getId());
        Message message2 = new Message("두번째 메세지 입니다.", aaron.getId(), channel1.getId());
        Message message3 = new Message("세번째 메세지 입니다.", baron.getId(), channel1.getId());

        MessageService messageService = new JCFMessageService();
        messageService.save(message1);
        messageService.save(message2);
        messageService.save(message3);
        System.out.println("단건 조회");
        System.out.println(messageService.find(message1.getId()));
        System.out.println("다건 조회");
        System.out.println(messageService.findAll());
        Message newMessage1 = messageService.find(message1.getId());
        newMessage1.changeMessage("변경 된 메세지 입니다.");
        System.out.println(messageService.find(newMessage1.getId()));
        Message deleteMessage = messageService.find(message3.getId());
        messageService.delete(deleteMessage.getId());

        System.out.println("최종 조회");
        System.out.println(messageService.findAll());

    }
}
