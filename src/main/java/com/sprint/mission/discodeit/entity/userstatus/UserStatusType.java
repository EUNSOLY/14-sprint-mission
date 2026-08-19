package com.sprint.mission.discodeit.entity.userstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum UserStatusType {
    ONLINE("온라인"),
    OFFLINE("오프라인");

    String status;


    @JsonValue
    public String getStatus() {
        return this.status;
    }

    @JsonCreator
    public static UserStatusType to(String status) {
        for (UserStatusType statusType : UserStatusType.values()) {
            if (statusType.getStatus().equals(status)) {
                return statusType;
            }
        }
        throw new RuntimeException("일치하는 유저상태가 없습니다.");
    }
}
