package com.sprint.mission.discodeit.common.error.handler;

import com.sprint.mission.discodeit.common.dto.ApiResponse;
import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.error.exception.GlobalCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 우리가 직접 정의한 비즈니스 예외 처리
    @ExceptionHandler(GlobalCustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(GlobalCustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getMessage(), e);
        return ApiResponse.toFail(e.getErrorCode());
    }

    // 2. 그 외 모든 예외 처리 (예상치 못한 서버 에러)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("handleException throw Exception : {}", e.getMessage(), e);
        return ApiResponse.toFail(CustomStatusCode.INTERNAL_SERVER_ERROR);
    }
}

