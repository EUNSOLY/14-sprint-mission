package com.sprint.mission.discodeit.common.error.exception;

import com.sprint.mission.discodeit.common.error.dto.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 2. 커스텀 예외 클래스 생성
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
