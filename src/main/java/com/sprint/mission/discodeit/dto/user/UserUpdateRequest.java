package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 1, max = 50, message = "이름은 1자리 이상 50자리 이하만 가능합니다.")
        String newUsername,

        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Size(min = 1, max = 100, message = "이메일은 1자리 이상 100자리 이하만 가능합니다.")

        String newEmail,
        @Size(min = 4, max = 60, message = "비밀번호는 4자리 이상 60자리 이하만 가능합니다.")
        String newPassword
) {
}
