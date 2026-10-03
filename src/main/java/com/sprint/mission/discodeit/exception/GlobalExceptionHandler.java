package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentException;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusException;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handle(UserException e) {
        log.warn("UserException :  {}", e.getErrorCode().getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.builder()
                        .timestamp(e.getTimestamp())
                        .code(e.getErrorCode().name())
                        .details(e.getDetails())
                        .exceptionType(e.getClass().getSimpleName())
                        .status(e.getErrorCode().getStatus().value())
                        .build());
    }

    @ExceptionHandler(ChannelException.class)
    public ResponseEntity<ErrorResponse> handle(ChannelException e) {
        log.warn("ChannelException : {}", e.getErrorCode().getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.builder()
                        .timestamp(e.getTimestamp())
                        .code(e.getErrorCode().name())
                        .details(e.getDetails())
                        .exceptionType(e.getClass().getSimpleName())
                        .status(e.getErrorCode().getStatus().value())
                        .build());
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErrorResponse> handle(MessageException e) {
        log.warn("MessageException : {}", e.getErrorCode().getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.builder()
                        .timestamp(e.getTimestamp())
                        .code(e.getErrorCode().name())
                        .details(e.getDetails())
                        .exceptionType(e.getClass().getSimpleName())
                        .status(e.getErrorCode().getStatus().value())
                        .build());
    }

    @ExceptionHandler(UserStatusException.class)
    public ResponseEntity<ErrorResponse> handle(UserStatusException e) {
        log.warn("UserStatusException : {}", e.getErrorCode().getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.builder()
                        .timestamp(e.getTimestamp())
                        .code(e.getErrorCode().name())
                        .message(e.getMessage())
                        .details(e.getDetails())
                        .exceptionType(e.getClass().getSimpleName())
                        .status(e.getErrorCode().getStatus().value())
                        .build());
    }

    @ExceptionHandler(ReadStatusException.class)
    public ResponseEntity<ErrorResponse> handle(ReadStatusException e) {
        log.warn("ReadStatusException : {}", e.getErrorCode().getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.builder()
                        .timestamp(e.getTimestamp())
                        .code(e.getErrorCode().name())
                        .message(e.getMessage())
                        .details(e.getDetails())
                        .exceptionType(e.getClass().getSimpleName())
                        .status(e.getErrorCode().getStatus().value())
                        .build());
    }

    @ExceptionHandler(BinaryContentException.class)
    public ResponseEntity<ErrorResponse> handle(BinaryContentException e) {
        log.warn("BinaryContentException : {}", e.getErrorCode().getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.builder()
                        .timestamp(e.getTimestamp())
                        .code(e.getErrorCode().name())
                        .message(e.getMessage())
                        .details(e.getDetails())
                        .exceptionType(e.getClass().getSimpleName())
                        .status(e.getErrorCode().getStatus().value())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException : {}", e.getMessage());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, Object> details = fieldErrors.stream()
                .collect(Collectors.toMap(
                                FieldError::getField,
                                value -> Optional.ofNullable(value.getDefaultMessage()).orElse("입력값을 확인하세요"),
//                        value -> String.format("%s / 현재 입력값 : %s",
//                                value.getDefaultMessage(),
//                                Optional.ofNullable(value.getRejectedValue()).orElse("없음")
//                        ),
                                (a, b) -> a) // 같은 필드에 에러가 여러 개면 첫 번째 값 유지
                );


        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .message("입력값 검증에 실패했습니다.")
                        .details(details)
                        .code(e.getClass().getSimpleName())
                        .exceptionType(e.getClass().getSimpleName())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build());
    }

    // 알수 없는 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        log.error("Exception : {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .message(e.getMessage())
                        .code(e.getClass().getSimpleName())
                        .exceptionType(e.getClass().getSimpleName())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build());
    }
}
