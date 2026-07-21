package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {

        // User
        System.out.println("=============== 사용자 ===============");
        User aaron = new User("Aaron", "010-1111-1111", "aaron", UserStatus.ONLINE);
        User baron = new User("Baron", "010-2222-2222", "baron", UserStatus.ONLINE);
        User caron = new User("Caron", "010-3333-3333", "caron", UserStatus.AWAY);

        // 사용자 저장
        UserService userService = new JCFUserService();
        userService.save(aaron);
        userService.save(baron);
        userService.save(caron);

        System.out.println("단건 조회");
        System.out.println(userService.find(aaron.getId()));
        System.out.println("다건 조회");
        System.out.println(userService.findAll());
        // 사용자 수정
        User newAaron = userService.find(aaron.getId());
        newAaron.changeName("Aaron_2");
        newAaron.changeNickname("aarorong");
        newAaron.changeStatus(UserStatus.toUserStatus("자리비움"));
        userService.update(newAaron.getId(), newAaron);
        System.out.println(userService.find(newAaron.getId()));
        // 사용자 삭제
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
        // 채널 저장
        channelService.save(channel1);
        channelService.save(channel2);
        channelService.save(channel3);
        System.out.println("단건 조회");
        System.out.println(channelService.find(channel1.getId()));
        System.out.println("다건 조회");
        System.out.println(channelService.findAll());
        // 채널 수정
        Channel newChannel1 = channelService.find(channel1.getId());
        newChannel1.changeName("new_Channel1");
        channelService.update(newChannel1.getId(), newChannel1);
        System.out.println(channelService.find(newChannel1.getId()));
        // 채널 삭제
        Channel deleteChannel = channelService.find(channel3.getId());
        channelService.delete(deleteChannel.getId());

        System.out.println("최종 조회");
        System.out.println(channelService.findAll());

        System.out.println("=============== 메세지 ===============");
        Message message1 = new Message("첫번째 메세지 입니다.", aaron.getId(), channel2.getId());
        Message message2 = new Message("두번째 메세지 입니다.", baron.getId(), channel1.getId());
        Message message3 = new Message("세번째 메세지 입니다.", baron.getId(), channel1.getId());
        Message message4 = new Message("아론의 두번째 메세지 입니다.", aaron.getId(), channel1.getId());
        Message message5 = new Message("아론의 세번째 메세지 입니다.", aaron.getId(), channel1.getId());


        MessageService messageService = new JCFMessageService(userService, channelService);
        // 메세지 저장
        messageService.save(message1);
        messageService.save(message2);
        messageService.save(message3);
        messageService.save(message4);
        messageService.save(message5);

        System.out.println("단건 조회");
        System.out.println(messageService.find(message1.getId()));
        System.out.println("다건 조회");
        System.out.println(messageService.findAll());
        // 메세지 수정
        Message newMessage1 = messageService.find(message1.getId());
        newMessage1.changeMessage("변경 된 메세지 입니다.");
        messageService.update(newMessage1.getId(), newMessage1);
        System.out.println(messageService.find(newMessage1.getId()));
        // 메세지 삭제
        Message deleteMessage = messageService.find(message3.getId());
        messageService.delete(deleteMessage.getId());

        System.out.println("최종 조회");
        System.out.println(messageService.findAll());

        // 특정 유저 메세지
        System.out.println("-------" + aaron.getName() + "님의 메세지만 조회합니다. -------");

        List<Message> userMessage = messageService.findByUserId(aaron.getId());
        userMessage.forEach(System.out::println);

        // 특정 채널 메세지
        System.out.println("------- 채널명 : [" + channel2.getName() + "] 메세지만 조회합니다. -------");

        List<Message> channelMessage = messageService.findByChannelId(channel2.getId());
        channelMessage.forEach(System.out::println);

        // 특정 채널 + 회원 메세지
        System.out.println("------- 채널명 : [" + channel1.getName() + "] - " + aaron.getName() + "님 메세지만 조회합니다. -------");

        List<Message> channelByUserMessage = messageService.findByChannelIdAndUserId(aaron.getId(), channel1.getId());
        channelByUserMessage.forEach(System.out::println);

        //심화 검증 테스트
//        Message message6 = new Message("네번째 메세지 입니다.", UUID.randomUUID(), channel1.getId());
//        messageService.save(message6);

    }
}
