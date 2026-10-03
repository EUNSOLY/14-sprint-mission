package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "이름은 필수입니다.")
        @Size(min = 1, max = 50, message = "이름은 1자리 이상 50자리 이하만 가능합니다.")
        String username,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Size(min = 1, max = 100, message = "이메일은 1자리 이상 100자리 이하만 가능합니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 4, max = 60, message = "비밀번호는 4자리 이상 60자리 이하만 가능합니다.")
        String password
) {

    public User toEntity() {
        return User.create(this.username, this.email, this.password);
    }
}
