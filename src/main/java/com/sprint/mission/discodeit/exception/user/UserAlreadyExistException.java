package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class UserAlreadyExistException extends UserException {

    private UserAlreadyExistException(Map<String, Object> details) {
        super(ErrorCode.DUPLICATE_USERNAME, details);
    }

    public static UserAlreadyExistException of(Map<String, Object> details) {
        return new UserAlreadyExistException(details);
    }
}
