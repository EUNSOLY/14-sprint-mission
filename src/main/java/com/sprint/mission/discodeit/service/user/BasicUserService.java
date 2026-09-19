package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.common.dto.CustomStatusCode;
import com.sprint.mission.discodeit.common.exception.GlobalCustomException;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserValidator userValidator;
    private final BinaryContentValidator binaryContentValidator;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User save(
            UserCreateRequest requestDto,
            BinaryContentCreateRequestDto profileCreateRequest
    ) {
        boolean hasDuplicateName = userRepository.existsByUsername(requestDto.username());

        boolean hasDuplicateEmail = userRepository.existsByEmail(requestDto.email());

        // 이름 중복 검증
        if (hasDuplicateName) {
            throw new GlobalCustomException(CustomStatusCode.DUPLICATE_NAME);
        }
        // 이메일 중복 검증
        if (hasDuplicateEmail) {
            throw new GlobalCustomException(CustomStatusCode.DUPLICATE_EMAIL);
        }

        User savedUser = requestDto.toEntity(); // 저장될 User Entity

        // 프로필 있으면 생성 후 UUID 반환
        BinaryContent profile = Optional.ofNullable(profileCreateRequest)
                .map((profileRequest) -> {
                    BinaryContent binaryContent = profileRequest.toEntity();
                    return binaryContentRepository.save(binaryContent);
                }).orElse(null);

        savedUser.updateProfile(profile); // 프로필 ID 업데이트
        userRepository.save(savedUser); // 저장

        UserStatus userStatus = UserStatus.create(savedUser); // User 로그인 일시 핸들러 Entity 생성
        userStatusRepository.save(userStatus); // UserStatus 저장

        return savedUser;
    }

    @Override
    public UserDto find(UserIdRequestDto requestDto) {
        User currentUser = userValidator.getOrThrow(requestDto.getId());

        return userMapper.toDto(currentUser);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public User update(
            UserIdRequestDto userId, UserUpdateRequest userUpdateRequest,
            BinaryContentCreateRequestDto profileCreateRequest
    ) {
        User currentUser = userValidator.getOrThrow(userId.getId());

        boolean hasDuplicateName = userRepository.existsByUsername(userUpdateRequest.newUsername());

        boolean hasDuplicateEmail = userRepository.existsByEmail(userUpdateRequest.newEmail());

        // 이름 중복 검증
        if (hasDuplicateName) {
            throw new GlobalCustomException(CustomStatusCode.DUPLICATE_NAME);
        }
        // 이메일 중복 검증
        if (hasDuplicateEmail) {
            throw new GlobalCustomException(CustomStatusCode.DUPLICATE_EMAIL);
        }

        currentUser.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(), userUpdateRequest.newPassword());

        // 새로운 프로필 데이터가 들어오면 기존 프로필 데이터 삭제 -> 신규 프로필 저장 -> User 엔티티 연계
        Optional.ofNullable(profileCreateRequest)
                .ifPresent(profileCommand -> {
                    // 이미 프로필이 있다면 제거
                    Optional.ofNullable(currentUser.getProfile())
                            .ifPresent(content -> {
                                binaryContentValidator.getOrThrow(content.getId());
                                binaryContentRepository.delete(content);
                            });

                    // 프로필 저장
                    BinaryContent binaryContent = profileCommand.toEntity();
                    binaryContentRepository.save(binaryContent);
                    currentUser.updateProfile(binaryContent);
                });

        return currentUser;
    }

    @Override
    @Transactional
    public void delete(UserIdRequestDto requestDto) {
        User deleteUser = userValidator.getOrThrow(requestDto.getId());

        userStatusRepository.deleteByUserId(requestDto.getId()); // 로그인 상태 삭제

        Optional.ofNullable(deleteUser.getProfile())
                .ifPresent(binaryContentRepository::delete);

        userRepository.delete(deleteUser); // 유저 삭제
    }

    @Override
    @Transactional
    public UserStatus updateUserOnlineStatus(UserIdRequestDto requestDto) {
        User user = userValidator.getOrThrow(requestDto.getId());
        UserStatus status = userStatusRepository.findByUserId(user.getId())
                .orElse(UserStatus.create(user));
        status.updateLastAccessAt();
        return status;
    }
}
