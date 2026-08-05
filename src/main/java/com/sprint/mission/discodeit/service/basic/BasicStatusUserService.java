package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicStatusUserService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public void save(UserStatusCreateRequestDto request) {
        this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        this.userStatusRepository.findByUserId(request.getUserId())
                .ifPresent(status -> {
                    throw new RuntimeException("이미 존재하는 데이터 입니다.");
                });

        this.userStatusRepository.save(request.toEntity());

    }

    @Override
    public UserStatus findById(UUID id) {
        return this.userStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("데이터가 존재하지 않습니다."));
    }

    @Override
    public List<UserStatus> findAll() {
        return this.userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatusUpdateRequestDto request) {
        UserStatus updateUserStatus = this.userStatusRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("데이터가 존재하지 않습니다."));

        updateUserStatus.updateLastAccessAt();
        this.userStatusRepository.update(updateUserStatus);
        return updateUserStatus;
    }

    @Override
    public UserStatus updateByUserId(UUID userId) {
        UserStatus updateUserStatus = this.userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("데이터가 존재하지 않습니다."));

        updateUserStatus.updateLastAccessAt();

        this.userStatusRepository.update(updateUserStatus);
        return updateUserStatus;
    }

    @Override
    public void delete(UUID id) {
        this.userStatusRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("데이터가 존재하지 않습니다."));

        this.userStatusRepository.delete(id);
    }
}
