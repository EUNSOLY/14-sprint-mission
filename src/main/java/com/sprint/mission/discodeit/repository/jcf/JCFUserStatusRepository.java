package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data = new HashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        this.data.put(userStatus.getId(), userStatus);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return this.data.values().stream()
                .filter(data -> data.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public UserStatus update(UserStatus userStatus) {
        this.data.replace(userStatus.getId(), userStatus);
        return this.data.get(userStatus.getId());
    }

    @Override
    public void delete(UUID id) {
        this.data.remove(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        this.data.values()
                .forEach(status -> {
                    if (status.getUserId().equals(userId)) {
                        this.data.remove(status.getId());
                    }
                });
    }
}
