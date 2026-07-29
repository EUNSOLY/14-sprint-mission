package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {
    private static final Map<UUID, User> data = new HashMap<>();
    private static JCFUserRepository INSTANCE;

    private JCFUserRepository() {
    }

    // 싱글턴
    public static JCFUserRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JCFUserRepository();
        }
        return INSTANCE;
    }


    @Override
    public void save(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public User findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void update(UUID id, User user) {
        data.replace(id, user);
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
