package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.config.FileProperties;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository extends FileAbstractRepository implements BinaryContentRepository {
    private static final String FILE_NAME = "binary.dir";
    private final Map<UUID, BinaryContent> cache = new HashMap<>();

    public FileBinaryContentRepository(FileProperties properties) {
        super(properties.getFileDirectory(), FILE_NAME);
        cache.putAll(super.load());
    }

    @PostConstruct
    public void init() {
        cache.putAll(super.load());
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        this.cache.put(binaryContent.getId(), binaryContent);
        super.fileSave(this.cache);
        return this.cache.get(binaryContent.getId());
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(this.cache.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return this.cache.values().stream()
                .filter(binaryContent -> ids.contains(binaryContent.getId()))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        this.cache.remove(id);
        super.fileSave(this.cache);
    }
}
