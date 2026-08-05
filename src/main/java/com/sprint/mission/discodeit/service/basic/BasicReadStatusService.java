package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public void save(ReadStatusCreateRequestDto request) {
        this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        this.channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 채널입니다."));

        // ifPresent : 값이 있다면 실행
        this.readStatusRepository.findByUserIdAndChannelId(request.getUserId(), request.getChannelId())
                .ifPresent(status -> {
                    throw new RuntimeException("이미 존재하는 데이터입니다. 신규로 생성하실 수 없습니다.");
                });

        this.readStatusRepository.save(request.toEntity()); // 저장
    }

    @Override
    public ReadStatus find(UUID id) {
        return this.readStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 데이터 입니다."));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return this.readStatusRepository.findByUserId(userId);
    }

    @Override
    public ReadStatus update(ReadStatusUpdateRequestDto requestDto) {
        ReadStatus updateReadStatus = this.readStatusRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("수정이 가능한 데이터가 존재하지 않습니다."));

        updateReadStatus.updateLastReadMessageAt();

        return this.readStatusRepository.update(updateReadStatus);
    }

    @Override
    public void delete(UUID id) {
        this.readStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("삭제 가능한 데이터가 존재하지 않습니다."));

        this.readStatusRepository.delete(id);
    }
}
