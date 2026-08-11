package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.config.FileProperties;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository extends FileAbstractRepository implements MessageRepository {
    private static final String FILE_NAME = "message.dir";
    private final Map<UUID, Message> cache = new HashMap<>();

    public FileMessageRepository(FileProperties properties) {
        super(properties.getFileDirectory(), FILE_NAME);
        cache.putAll(super.load());
    }

    @Override
    public void save(Message message) {
        this.cache.put(message.getId(), message); // 신규 데이터 저장
        super.fileSave(this.cache);
    }


    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(this.cache.get(id));
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        return this.cache.values().stream()
                .filter(message -> message.getUserId().equals(userId))
                .toList();

    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return this.cache.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();


    }

    @Override
    public List<Message> findByChannelIdAndUserId(UUID userId, UUID channelId) {
        return this.cache.values().stream()
                .filter(message -> message.getUserId().equals(userId))
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<Message> findAll() {
        return this.cache.values().stream().toList();
    }

    @Override
    public void update(UUID id, Message message) {
        this.cache.replace(id, message); // 데이터 저장해
        super.fileSave(this.cache); // 데이터 파일로 만들어

    }

    @Override
    public void delete(UUID id) {
        this.cache.remove(id);
        super.fileSave(this.cache);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        this.cache.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .forEach(message -> this.cache.remove(message.getId()));
        super.fileSave(this.cache);
    }
}
