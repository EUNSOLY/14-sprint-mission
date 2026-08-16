package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.error.exception.GlobalCustomException;
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

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public void save(UserStatusCreateRequestDto request) {
        this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.USER_NOT_FOUND));


        this.userStatusRepository.findByUserId(request.getUserId())
                .ifPresent(status -> {
                    throw new GlobalCustomException(CustomStatusCode.DUPLICATE_DATA);
                });

        this.userStatusRepository.save(request.toEntity());

    }

    @Override
    public UserStatus findById(UserStatusIdRequestDto requestDto) {
        return this.userStatusRepository.findById(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));
    }

    @Override
    public List<UserStatus> findAll() {
        return this.userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatusUpdateRequestDto request) {
        UserStatus updateUserStatus = this.userStatusRepository.findById(request.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));

        updateUserStatus.updateLastAccessAt();
        this.userStatusRepository.update(updateUserStatus);
        return updateUserStatus;
    }

    @Override
    public UserStatus updateByUserId(UserIdRequestDto requestDto) {
        UserStatus updateUserStatus = this.userStatusRepository.findByUserId(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));

        updateUserStatus.updateLastAccessAt();

        this.userStatusRepository.update(updateUserStatus);
        return updateUserStatus;
    }

    @Override
    public void delete(UserStatusIdRequestDto requestDto) {
        this.userStatusRepository.findByUserId(requestDto.getId())
                .orElseThrow(() -> new GlobalCustomException(CustomStatusCode.DATA_NOT_FOUND));

        this.userStatusRepository.delete(requestDto.getId());
    }
}
