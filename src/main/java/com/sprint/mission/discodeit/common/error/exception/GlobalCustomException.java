package com.sprint.mission.discodeit.common.error.exception;

import com.sprint.mission.discodeit.common.error.dto.ErrorCode;
import lombok.Getter;

@Getter
// 2. 커스텀 예외 클래스 생성
public class GlobalCustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detail;

    public GlobalCustomException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage() + (detail != null ? " " + detail : ""));
        this.errorCode = errorCode;
        this.detail = detail;
    }

    public GlobalCustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = null;
    }
}
