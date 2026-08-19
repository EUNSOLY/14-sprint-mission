package com.sprint.mission.discodeit.common.exception;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import lombok.Getter;

@Getter
public class GlobalCustomException extends RuntimeException {
    private final CustomStatusCode errorCode;

    public GlobalCustomException(CustomStatusCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
