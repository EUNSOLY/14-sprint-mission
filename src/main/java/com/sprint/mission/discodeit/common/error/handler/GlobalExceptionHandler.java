package com.sprint.mission.discodeit.common.error.handler;

import com.sprint.mission.discodeit.common.error.dto.ErrorCode;
import com.sprint.mission.discodeit.common.error.dto.ErrorResponseDto;
import com.sprint.mission.discodeit.common.error.exception.GlobalCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
// 3. 전역 예외 핸들러
public class GlobalExceptionHandler {

    // 1. 우리가 직접 정의한 비즈니스 예외 처리
    @ExceptionHandler(GlobalCustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(GlobalCustomException e) {
        String resultMessage = Objects.nonNull(e.getDetail()) ? e.getErrorCode().getMessage() + " " + e.getDetail() : e.getErrorCode().getMessage();
        log.error("handleCustomException throw CustomException : {}", resultMessage, e);
        return ErrorResponseDto.toResponseEntity(e.getErrorCode(), e.getDetail());
    }

    // 2. 그 외 모든 예외 처리 (예상치 못한 서버 에러)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        log.error("handleException throw Exception : {}", e.getMessage(), e);
        return ErrorResponseDto.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}

