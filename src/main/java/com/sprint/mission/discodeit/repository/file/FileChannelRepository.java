package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelRepository extends FileAbstractRepository implements ChannelRepository {
    private static final String FILE_NAME = "channel.dir";
    private final Map<UUID, Channel> cache = new HashMap<>();

    public FileChannelRepository() {
        super(FILE_NAME);
        cache.putAll(super.load());
    }
    
    @Override
    public void save(Channel channel) {
        this.cache.put(channel.getId(), channel); // 신규 데이터 저장
        super.fileSave(this.cache);
    }

    @Override
    public Channel findById(UUID id) {
        return this.cache.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return this.cache.values().stream().toList();
    }

    @Override
    public void update(UUID id, Channel channel) {
        this.cache.replace(id, channel); // 데이터 저장해
        super.fileSave(this.cache);
    }

    @Override
    public void delete(UUID id) {
        this.cache.remove(id);
        super.fileSave(this.cache);
    }
}
