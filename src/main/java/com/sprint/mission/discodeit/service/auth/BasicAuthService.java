package com.sprint.mission.discodeit.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final SessionRegistry sessionRegistry;

    @Override
    public boolean isOnline(UUID userId) {
        return sessionRegistry.getAllPrincipals().stream()
                .filter(object -> object instanceof DiscodietUserDetails d && d.getUserDto().id().equals(userId))
                .anyMatch(object -> !sessionRegistry.getAllSessions(object, false).isEmpty());
    }
}
