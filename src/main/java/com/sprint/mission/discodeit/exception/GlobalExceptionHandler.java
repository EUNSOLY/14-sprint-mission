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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        log.warn("Exception : {}", e.getMessage());

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
