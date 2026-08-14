package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.validator.BinaryContentValidator;
import com.sprint.mission.discodeit.common.validator.UserValidator;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserValidator userValidator;
    private final BinaryContentValidator binaryContentValidator;

    @Override
    public void save(
            UserCreateRequestDto requestDto,
            BinaryContentCreateRequestDto profileCreateRequest
    ) {
        boolean hasDuplicateName = userRepository.existsByName(requestDto.getName());

        boolean hasDuplicateEmail = userRepository.existsByEmail(requestDto.getEmail());

        // 이름 중복 검증
        if (hasDuplicateName) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다. 다른 이름을 입력해주세요.");
        }
        // 이메일 중복 검증
        if (hasDuplicateEmail) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다. 다른 이메일을 입력해주세요.");
        }

        User savedUser = requestDto.toEntity(); // 저장될 User Entity

        UserStatus userStatus = new UserStatus(savedUser.getId()); // User 로그인 일시 핸들러 Entity 생성
        userStatusRepository.save(userStatus); // UserStatus 저장

        // 프로필 있으면 생성 후 UUID 반환
        UUID profileId = Optional.ofNullable(profileCreateRequest)
                .map((profileRequest) -> {
                    BinaryContent binaryContent = profileRequest.toEntity();
                    return binaryContentRepository.save(binaryContent).getId();
                }).orElse(null);

        savedUser.updateProfile(profileId); // 프로필 ID 업데이트
        userRepository.save(savedUser); // 저장
    }

    @Override
    public UserResponseDto find(UserIdRequestDto requestDto) {
        User currentUser = userValidator.getOrThrow(requestDto.getId());
        boolean userStatus = userStatusRepository.findByUserId(currentUser.getId())
                .map(UserStatus::isCurrentlyLoggedIn)
                .orElse(false);

        UserStatusType userStatusType = userStatus ? UserStatusType.ONLINE : UserStatusType.OFFLINE;

        return UserResponseDto.from(currentUser, userStatusType);
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    boolean userStatus = userStatusRepository.findByUserId(user.getId())
                            .map(UserStatus::isCurrentlyLoggedIn)
                            .orElse(false);

                    UserStatusType userStatusType = userStatus ? UserStatusType.ONLINE : UserStatusType.OFFLINE;
                    return UserResponseDto.from(user, userStatusType);
                })
                .toList();
    }

    @Override
    public void update(
            UserUpdateRequestDto updateRequestDto,
            BinaryContentCreateRequestDto profileCreateRequest
    ) {
        User currentUser = userValidator.getOrThrow(updateRequestDto.getId());

        currentUser.update(updateRequestDto.getName(), updateRequestDto.getEmail(), updateRequestDto.getPassword());

        // 새로운 프로필 데이터가 들어오면 기존 프로필 데이터 삭제 -> 신규 프로필 저장 -> User 엔티티 연계
        Optional.ofNullable(profileCreateRequest)
                .ifPresent(profileRequest -> {
                    // 이미 프로필이 있다면 제거
                    Optional.ofNullable(currentUser.getProfileId())
                            .ifPresent(contentId -> {
                                binaryContentValidator.getOrThrow(contentId);
                                binaryContentRepository.delete(contentId);
                            });

                    // 프로필 저장
                    BinaryContent binaryContent = profileRequest.toEntity();
                    binaryContentRepository.save(binaryContent);
                    currentUser.updateProfile(binaryContent.getId());
                });

        userRepository.update(currentUser.getId(), currentUser);
    }

    @Override
    public void delete(UserIdRequestDto requestDto) {
        User deleteUser = userValidator.getOrThrow(requestDto.getId());

        userStatusRepository.deleteByUserId(requestDto.getId()); // 로그인 상태 삭제

        Optional.ofNullable(deleteUser.getProfileId())
                .ifPresent(binaryContentRepository::delete);

        userRepository.delete(requestDto.getId()); // 유저 삭제
    }

    @Override
    public void updateUserOnlineStatus(UserIdRequestDto requestDto) {
        User user = userValidator.getOrThrow(requestDto.getId());
        UserStatus status = userStatusRepository.findByUserId(user.getId())
                .orElse(new UserStatus(requestDto.getId()));
        status.updateLastAccessAt();
        userStatusRepository.update(status);
    }
}
