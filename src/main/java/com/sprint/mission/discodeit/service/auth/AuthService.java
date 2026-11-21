package com.sprint.mission.discodeit.service.auth;


import java.util.UUID;

public interface AuthService {
    boolean isOnline(UUID userId);
}
