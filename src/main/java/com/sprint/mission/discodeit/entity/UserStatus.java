package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public enum UserStatus {
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    AWAY("자리비움"),
    DO_NOT_DISTURB("방해금지");
    String status;

    public static UserStatus toUserStatus(String status) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.getStatus().equals(status)) {
                return userStatus;
            }
        }
        throw new RuntimeException("알맞지 않은 유형입니다.");
    }
}