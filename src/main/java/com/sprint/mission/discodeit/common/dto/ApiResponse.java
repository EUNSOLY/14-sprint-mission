package com.sprint.mission.discodeit.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값은 자동 제외
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    private final boolean success;
    private final int code;
    private final String message;
    private final T data;
    private final List<ErrorResponse> errors;

    public static <T> ResponseEntity<ApiResponse<T>> toSuccess(CustomStatusCode code, T data) {
        return ResponseEntity
                .status(code.getStatus())
                .body(ApiResponse.<T>builder()
                        .success(true)
                        .code(code.getCode())
                        .data(data)
                        .message(code.getMessage())
                        .build()
                );
    }

    public static ResponseEntity<ApiResponse<Void>> toFail(CustomStatusCode code, List<ErrorResponse> errors) {
        return ResponseEntity
                .status(code.getStatus())
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .code(code.getCode())
                        .message(code.getMessage())
                        .errors(errors)
                        .build()
                );
    }

    public static ResponseEntity<ApiResponse<Void>> toFail(CustomStatusCode code) {
        return ResponseEntity
                .status(code.getStatus())
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .code(code.getCode())
                        .message(code.getMessage())
                        .build()
                );
    }
}
