package com.sprint.mission.discodeit.common.error.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
// 1. 에러 코드 정의
public enum ErrorCode {
    // 404 NOT_FOUND: 리소스를 찾을 수 없음
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "유저가 존재하지 않습니다."),
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "채널이 존재하지 않습니다."),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "프로필 사진이 존재하지 않습니다."),
    CONTENT_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "파일이 존재하지 않습니다."),

    // 500 INTERNAL_SERVER_ERROR: 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.NOT_FOUND.value(), "서버 내부 에러가 발생했습니다.");

    private final HttpStatus status;   // HTTP 상태코드, ResponseEntity에 전달할 때 사용
    private final int code;            // 커스텀 에러 코드 (기본HTTP 상태코드, 다른 예외상황에 따라 변경가능)
    private final String message;      // 사용자에게 보여줄 메시지

}
