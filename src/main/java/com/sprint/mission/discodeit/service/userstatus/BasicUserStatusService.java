package com.sprint.mission.discodeit.service.userstatus;

import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusIdRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserValidator userValidator;

    @Override
    public void save(User user) {
        userValidator.getOrThrow(user.getId());

        this.userStatusRepository.findByUserId(user.getId());
        this.userStatusRepository.save(UserStatus.create(user));

    }

    @Override
    public UserStatus findById(UserStatusIdRequestDto requestDto) {
        return this.userStatusRepository.findById(requestDto.getId())
                .orElseThrow(() -> new UserStatusNotFoundException(Map.of("조회 ID", requestDto.getId())));
    }

    @Override
    public List<UserStatus> findAll() {
        return this.userStatusRepository.findAll();
    }

    @Override
    @Transactional
    public UserStatus update(UserStatusUpdateRequestDto request) {
        UserStatus updateUserStatus = this.findById(new UserStatusIdRequestDto(request.getId()));

        updateUserStatus.updateLastAccessAt();
        return updateUserStatus;
    }

    @Override
    @Transactional
    public UserStatus updateByUserId(UserIdRequestDto requestDto) {
        UserStatus updateUserStatus = this.userStatusRepository.findByUserId(requestDto.getId())
                .orElseThrow(() -> new UserStatusNotFoundException(Map.of("조회 ID", requestDto.getId())));

        updateUserStatus.updateLastAccessAt();

        return updateUserStatus;
    }

    @Override
    public void delete(UserStatusIdRequestDto requestDto) {
        UserStatus deletedEntity = this.userStatusRepository.findByUserId(requestDto.getId())
                .orElseThrow(() -> new UserStatusNotFoundException(Map.of("조회 ID", requestDto.getId())));

        this.userStatusRepository.delete(deletedEntity);
    }
}
