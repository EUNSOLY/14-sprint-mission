package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelException;

import java.util.Map;

public class BinaryContentNotFoundException extends ChannelException {

    public BinaryContentNotFoundException(Map<String, Object> details) {
        super(ErrorCode.BINARY_CONTENT_NOT_FOUND, details);
    }

}
