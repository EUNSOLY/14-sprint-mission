package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.common.FileStorageUtil;
import com.sprint.mission.discodeit.common.config.FileProperties;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.List;

public class JavaApplication {
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
        // 레포지토리 초기화
        FileProperties fileProperties = new FileProperties(".discodeit");
        UserRepository userRepository = new FileUserRepository(fileProperties);
        ChannelRepository channelRepository = new FileChannelRepository(fileProperties);
        MessageRepository messageRepository = new FileMessageRepository(fileProperties);
        ReadStatusRepository readStatusRepository = new FileReadStatusRepository(fileProperties);
        UserStatusRepository userStatusRepository = new FileUserStatusRepository(fileProperties);
        BinaryContentRepository binaryContentRepository = new FileBinaryContentRepository(fileProperties);
        FileStorageUtil fileStorageUtil = new FileStorageUtil(".upload-file-directory");


        // 서비스 초기화
        UserService userService = new BasicUserService(userRepository, userStatusRepository, binaryContentRepository, fileStorageUtil);
        ChannelService channelService = new BasicChannelService(channelRepository, readStatusRepository, messageRepository);
        MessageService messageService = new BasicMessageService(messageRepository, userRepository, channelRepository, binaryContentRepository, fileStorageUtil);

        // 셋업
        UserResponseDto user = setupUser(userService);
        ChannelResponseDto channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, user, channel);
    }
}
