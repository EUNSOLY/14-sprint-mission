package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();

    // Service의 경우 다중으로 인스턴스가 생성될 필요성이 없을 것 같아 싱글톤 패턴 추가
    private JCFUserService() {
    }

    private static class SingletonHolder {
        private static final JCFUserService SINGLETON_INSTANCE = new JCFUserService();
    }

    public static JCFUserService getInstance() {
        return SingletonHolder.SINGLETON_INSTANCE;
    }

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
