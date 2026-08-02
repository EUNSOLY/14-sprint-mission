package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileUserRepository extends FileAbstractRepository implements UserRepository {
    private static final String FILE_NAME = "user.dir";
    private final Map<UUID, User> cache = new HashMap<>();

    public FileUserRepository() {
        super(FILE_NAME);
        cache.putAll(super.load());
    }


    @Override
    public void save(User user) {
        this.cache.put(user.getId(), user); // 신규 데이터 저장
        super.fileSave(this.cache);
    }


    @Override
    public User findById(UUID id) {
        return this.cache.get(id);
    }

    @Override
    public List<User> findAll() {
        return this.cache.values().stream().toList();
    }

    @Override
    public void update(UUID id, User user) {
        this.cache.replace(id, user); // 데이터 저장해
        super.fileSave(this.cache);
    }

    @Override
    public void delete(UUID id) {
        this.cache.remove(id);
        super.fileSave(this.cache);
    }
}
