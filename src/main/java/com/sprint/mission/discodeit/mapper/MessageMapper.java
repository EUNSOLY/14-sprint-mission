package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.data.MessageDto;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final BinaryContentMapper binaryContentMapper;
    private final UserMapper userMapper;

    public MessageDto toDto(Message message) {
        UserDto userDto = userMapper.toDto(message.getAuthor());
        List<BinaryContentDto> binaryContentDtos = message.getAttachments().stream().map(binaryContentMapper::toDto).toList();
        return MessageDto.to(message, userDto, message.getChannel().getId(), binaryContentDtos);
    }
}
