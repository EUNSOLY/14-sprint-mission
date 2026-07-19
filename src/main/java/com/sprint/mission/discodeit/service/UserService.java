package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.*;

public interface UserService {
    void save(User user);
    User find(UUID id);
    List<User> findAll();
    void update(UUID id, User user);
    void delete(UUID id);
}
