package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.entity.user.Role;

import java.util.UUID;

public record UserRoleUpdateRequest(
        UUID userId,
        Role newRole
) {
}
