package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String name);

    List<User> findAll();

    void update(UUID id, User user);

    void delete(UUID id);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
