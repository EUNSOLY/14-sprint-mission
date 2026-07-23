package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public void save(User user) {
        data.put(user.getId(), user); // 저장로직
    }

    @Override
    public User find(UUID id) {
        if (!data.containsKey(id)) { // 비즈니스 로직
            return null;
        }
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList(); // 저장 로직
    }

    @Override
    public void update(UUID id, User user) {
        if (!data.containsKey(id)) { // 비즈니스 로직
            throw new RuntimeException("요청한 사용자의 데이터가 존재하지 않습니다.");
        }
        data.replace(id, user); // 저장로직

    }

    @Override
    public void delete(UUID id) {
        if (!data.containsKey(id)) { // 비즈니스 로직
            throw new RuntimeException("요청한 사용자의 데이터가 존재하지 않습니다.");
        }
        data.remove(id); // 저장 로직
    }
}
