package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public void save(UserStatusCreateRequestDto request) {
        this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        this.userStatusRepository.findByUserId(request.getUserId())
                .ifPresent(status -> {
                    throw new IllegalArgumentException("이미 존재하는 데이터 입니다.");
                });

        this.userStatusRepository.save(request.toEntity());

    }

    @Override
    public UserStatus findById(UserStatusIdRequestDto requestDto) {
        return this.userStatusRepository.findById(requestDto.getId())
                .orElseThrow(() -> new NoSuchElementException("데이터가 존재하지 않습니다."));
    }

    @Override
    public List<UserStatus> findAll() {
        return this.userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatusUpdateRequestDto request) {
        UserStatus updateUserStatus = this.userStatusRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("데이터가 존재하지 않습니다."));

        updateUserStatus.updateLastAccessAt();
        this.userStatusRepository.update(updateUserStatus);
        return updateUserStatus;
    }

    @Override
    public UserStatus updateByUserId(UserIdRequestDto requestDto) {
        UserStatus updateUserStatus = this.userStatusRepository.findByUserId(requestDto.getId())
                .orElseThrow(() -> new NoSuchElementException("데이터가 존재하지 않습니다."));

        updateUserStatus.updateLastAccessAt();

        this.userStatusRepository.update(updateUserStatus);
        return updateUserStatus;
    }

    @Override
    public void delete(UserStatusIdRequestDto requestDto) {
        this.userStatusRepository.findByUserId(requestDto.getId())
                .orElseThrow(() -> new NoSuchElementException("데이터가 존재하지 않습니다."));

        this.userStatusRepository.delete(requestDto.getId());
    }
}
