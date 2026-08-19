package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.config.FileProperties;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository extends FileAbstractRepository implements UserRepository {
    private static final String FILE_NAME = "user.dir";
    private final Map<UUID, User> cache = new HashMap<>();
    // 추상 글래스로 받아보기
    // 동일한 구조 추상클래스로 분할해보기


    public FileUserRepository(FileProperties properties) {
        super(properties.getFileDirectory(), FILE_NAME);
        cache.putAll(super.load());
    }


    @PostConstruct
    public void init() {
        cache.putAll(super.load());
    }

    @Override
    public void save(User user) {
        this.cache.put(user.getId(), user); // 신규 데이터 저장
        super.fileSave(this.cache);
    }


    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(this.cache.get(id));
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

    @Override
    public boolean existsByName(String name) {
        return this.cache.values().stream().anyMatch(user -> user.getName().equals(name));
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.cache.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }
}
