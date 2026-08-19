package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.config.FileProperties;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository extends FileAbstractRepository implements ChannelRepository {
    private static final String FILE_NAME = "channel.dir";
    private final Map<UUID, Channel> cache = new HashMap<>();

    public FileChannelRepository(FileProperties properties) {
        super(properties.getFileDirectory(), FILE_NAME);
        cache.putAll(super.load());
    }

    @Override
    public void save(Channel channel) {
        this.cache.put(channel.getId(), channel); // 신규 데이터 저장
        super.fileSave(this.cache);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(this.cache.get(id));
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
