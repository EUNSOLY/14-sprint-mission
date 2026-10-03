package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Public Channel 생성 정보")
public record PublicChannelCreateRequestDto(
        @NotBlank(message = "채널명은 필수 입니다.")
        @Size(min = 1, max = 100, message = "채널명은 1자리 이상 100자리 이하만 가능합니다.")
        String name,
        @NotBlank(message = "채널설명은 필수 입니다.")
        @Size(min = 1, max = 500, message = "채널설명은 1자리 이상 500자리 이하만 가능합니다.")
        String description
) {

    public Channel toEntity() {
        return Channel.create(ChannelType.PUBLIC, this.name, this.description);
    }
}
