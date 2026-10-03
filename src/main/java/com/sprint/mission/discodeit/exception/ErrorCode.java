package com.sprint.mission.discodeit.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ErrorCode {
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 사용자 이름입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이름 또는 패스워드가 일치하지 않습니다."),
    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 상태가 존재하지 않습니다."),
    INVALID_USER_STATUS_TYPE(HttpStatus.BAD_REQUEST, "일치하는 사용자 상태 타입이 없습니다."),

    // Channel
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널이 존재하지 않습니다."),
    PRIVATE_CHANNEL_UPDATE(HttpStatus.BAD_REQUEST, "비공개 채널은 수정이 불가합니다."),

    // Message
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메세지가 존재하지 않습니다."),

    // ReadStatus
    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "읽음 상태가 존재하지 않습니다."),

    // BinaryContent (파일)
    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "파일이 존재하지 않습니다."),
    FILE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 중 오류가 발생했습니다."),
    FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 불러오는 중 오류가 발생했습니다."),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 삭제할 수 없습니다."),

    // Server
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러가 발생했습니다.");

    HttpStatus status;
    String message;
}
