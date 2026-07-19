package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public User find(UUID id) {
        if(!data.containsKey(id)){
            return null;
        }
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void update(UUID id, User user) {
        if(!data.containsKey(id)){
            throw new RuntimeException("요청한 사용자의 데이터가 존재하지 않습니다.");
        }
        data.replace(id, user);

    }

    @Override
    public void delete(UUID id) {
        if(!data.containsKey(id)){
            throw new RuntimeException("요청한 사용자의 데이터가 존재하지 않습니다.");
        }
        data.remove(id);
    }
}
