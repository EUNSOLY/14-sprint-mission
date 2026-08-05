package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        this.data.put(binaryContent.getId(), binaryContent);
        return this.data.get(binaryContent.getId());
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return this.data.values().stream()
                .filter(binaryContent -> ids.contains(binaryContent.getId()))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        this.data.remove(id);
    }
}
