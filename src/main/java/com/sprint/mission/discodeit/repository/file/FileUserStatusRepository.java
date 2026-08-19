package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.config.FileProperties;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository extends FileAbstractRepository implements UserStatusRepository {
    private static final String FILE_NAME = "user-status.dir";
    private final Map<UUID, UserStatus> cache = new HashMap<>();

    public FileUserStatusRepository(FileProperties properties) {
        super(properties.getFileDirectory(), FILE_NAME);
        cache.putAll(super.load());
    }

    @Override
    public void save(UserStatus userStatus) {
        cache.put(userStatus.getId(), userStatus);
        super.fileSave(this.cache);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return cache.values().stream()
                .filter(data -> data.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return cache.values().stream().toList();
    }

    @Override
    public UserStatus update(UserStatus userStatus) {
        cache.replace(userStatus.getId(), userStatus);
        super.fileSave(this.cache);
        return cache.get(userStatus.getId());
    }

    @Override
    public void delete(UUID id) {
        cache.remove(id);
        super.fileSave(this.cache);

    }

    @Override
    public void deleteByUserId(UUID userId) {
        cache.values()
                .forEach(status -> {
                    if (status.getUserId().equals(userId)) {
                        cache.remove(status.getId());
                    }
                });
        super.fileSave(this.cache);
    }

}
