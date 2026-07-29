package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileMessageRepository extends FileAbstractRepository implements MessageRepository {
    private static final String FILE_NAME = "message.dir";
    private static FileMessageRepository INSTANCE;
    private final Map<UUID, Message> cache = new HashMap<>();

    private FileMessageRepository() {
        super(FILE_NAME);
        this.cache.putAll(super.load());
    }


    // 싱글턴
    public static FileMessageRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileMessageRepository();
        }
        return INSTANCE;
    }


    @Override
    public void save(Message message) {
        this.cache.put(message.getId(), message); // 신규 데이터 저장
        super.fileSave(this.cache);
    }


    @Override
    public Message findById(UUID id) {
        return this.cache.get(id);
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
    public List<Message> findByChannelIdAndUserId(UUID channelId, UUID userId) {
        return this.cache.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .filter(message -> message.getUserId().equals(userId))
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
}
