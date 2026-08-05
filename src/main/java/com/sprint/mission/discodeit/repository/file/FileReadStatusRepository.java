package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileReadStatusRepository extends FileAbstractRepository implements ReadStatusRepository {
    private static final String FILE_NAME = "read-status.dir";
    private final Map<UUID, ReadStatus> cache = new HashMap<>();

    public FileReadStatusRepository() {
        super(FILE_NAME);
        cache.putAll(super.load());

    }

    @Override
    public void save(ReadStatus readStatus) {
        this.cache.put(readStatus.getId(), readStatus);
        super.fileSave(this.cache);
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return this.cache.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAll() {
        return this.cache.values().stream().toList();
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return this.cache.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return this.cache.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public ReadStatus update(ReadStatus readStatus) {
        this.cache.replace(readStatus.getId(), readStatus);
        super.fileSave(this.cache);
        return this.cache.get(readStatus.getId());
    }

    @Override
    public void delete(UUID id) {
        this.cache.remove(id);
        super.fileSave(this.cache);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<UUID> toRemove = cache.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .map(ReadStatus::getId)
                .toList();

        toRemove.forEach(this.cache::remove);
        super.fileSave(this.cache);
    }
}
