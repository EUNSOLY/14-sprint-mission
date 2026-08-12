package com.sprint.mission.discodeit.common.error.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseDto {
    private final String name;     // Enum 명칭
    private final int code;        // Custom ErrorCode(HTTP 또는 Custom 들어옴)
    private final String message;  // 메세지


    public static ResponseEntity<ErrorResponseDto> toResponseEntity(ErrorCode errorCode) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode.name(), errorCode.getCode(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(errorResponseDto);
    }
}
