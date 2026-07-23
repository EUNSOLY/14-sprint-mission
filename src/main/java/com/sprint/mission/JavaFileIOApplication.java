package com.sprint.mission;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaFileIOApplication {
    public static void init(Path directory) {
        // 저장할 경로의 파일 초기화
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {

        // User
        System.out.println("=============== 사용자 ===============");
        User aaron = new User("Aaron", "010-1111-1111", "aaron", UserStatus.ONLINE);
        User baron = new User("Baron", "010-2222-2222", "baron", UserStatus.ONLINE);
        User caron = new User("Caron", "010-3333-3333", "caron", UserStatus.AWAY);

        // 사용자 저장
        FileUserService fileUserService = new FileUserService();
        fileUserService.save(aaron);
        fileUserService.save(baron);
        fileUserService.save(caron);

        User newAaron = fileUserService.find(aaron.getId());
        newAaron.changeName("Aaron_2");
        newAaron.changeNickname("aarorong");
        newAaron.changeStatus(UserStatus.toUserStatus("자리비움"));
        fileUserService.update(newAaron.getId(), newAaron);
        User deleteUser = fileUserService.find(caron.getId());
        fileUserService.delete(deleteUser.getId());
        System.out.println(fileUserService.findAll());


        // Channel
//        System.out.println("=============== 채널 ===============");
//        Channel channel1 = new Channel("코드_공유");
//        Channel channel2 = new Channel("모각코");
//        Channel channel3 = new Channel("소통");
//
//        FileChannelService fileChannelService = new FileChannelService();
//        fileChannelService.save(channel1);
//        fileChannelService.save(channel2);
//        fileChannelService.save(channel3);
//
//        Channel newChannel1 = fileChannelService.find(channel1.getId());
//        newChannel1.changeName("new_Channel1");
//        fileChannelService.update(newChannel1.getId(), newChannel1);
//
//        fileChannelService.update(newAaron.getId(), newChannel1);
//        System.out.println(fileChannelService.findAll());
//
//        System.out.println("=============== 메세지 ===============");
//        Message message1 = new Message("첫번째 메세지 입니다.", aaron.getId(), channel2.getId());
//        Message message2 = new Message("두번째 메세지 입니다.", baron.getId(), channel1.getId());
//        Message message3 = new Message("세번째 메세지 입니다.", baron.getId(), channel1.getId());
//        Message message4 = new Message("아론의 두번째 메세지 입니다.", aaron.getId(), channel1.getId());
//        Message message5 = new Message("아론의 세번째 메세지 입니다.", aaron.getId(), channel1.getId());
//
//        FileMessageService fileMessageService = new FileMessageService(fileUserService, fileChannelService);


    }
}
