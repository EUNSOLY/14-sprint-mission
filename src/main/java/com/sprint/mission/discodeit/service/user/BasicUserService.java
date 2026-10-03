package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.data.UserDto;
import com.sprint.mission.discodeit.dto.userstatus.data.UserStatusDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserDuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.UserDuplicateNameException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentValidator;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserValidator userValidator;
    private final BinaryContentValidator binaryContentValidator;
    private final UserMapper userMapper;
    private final UserStatusMapper userStatusMapper;
    private final BinaryContentStorage binaryContentStorage;

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
            log.warn("사용자 생성 실패 : 중복 이름");
            throw new UserDuplicateNameException(Map.of("요청 이름", requestDto.username()));
        }
        // 이메일 중복 검증
        if (hasDuplicateEmail) {
            log.warn("사용자 생성 실패 : 중복 이메일");
            throw new UserDuplicateEmailException(Map.of("요청 이메일", requestDto.username()));
        }

        User savedUser = requestDto.toEntity(); // 저장될 User Entity

        // 프로필 있으면 생성 후 UUID 반환
        BinaryContent profile = Optional.ofNullable(profileCreateRequest)
                .map((profileRequest) -> {
                    log.info("프로필 생성");
                    BinaryContent binaryContent = profileRequest.toEntity();
                    BinaryContent savedContent = binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(savedContent.getId(), profileRequest.bytes());
                    return savedContent;
                }).orElse(null);

        savedUser.updateProfile(profile); // 프로필 ID 업데이트
        log.info("사용자 생성");
        userRepository.save(savedUser); // 저장

        UserStatus userStatus = UserStatus.create(savedUser); // User 로그인 일시 핸들러 Entity 생성
        log.info("사용자 상태 생성");
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
    public UserDto update(
            UserIdRequestDto userId, UserUpdateRequest userUpdateRequest,
            BinaryContentCreateRequestDto profileCreateRequest
    ) {
        User currentUser = userValidator.getOrThrow(userId.getId());

        boolean hasDuplicateName = userRepository.existsByUsername(userUpdateRequest.newUsername());

        boolean hasDuplicateEmail = userRepository.existsByEmail(userUpdateRequest.newEmail());

        // 이름 중복 검증
        if (hasDuplicateName) {
            log.warn("사용자 수정 실패 : 중복 이름");
            throw new UserDuplicateNameException(Map.of("요쳥 이름", userUpdateRequest.newUsername()));
        }
        // 이메일 중복 검증
        if (hasDuplicateEmail) {
            log.warn("사용자 수정 실패 : 중복 이메일");
            throw new UserDuplicateEmailException(Map.of("요청 이메일", userUpdateRequest.newEmail()));
        }

        currentUser.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(), userUpdateRequest.newPassword());

        // 새로운 프로필 데이터가 들어오면 기존 프로필 데이터 삭제 -> 신규 프로필 저장 -> User 엔티티 연계
        Optional.ofNullable(profileCreateRequest)
                .ifPresent(profileCommand -> {
                    // 이미 프로필이 있다면 제거
                    log.info("사용자 수정 - 기존 프로필 이미지 제거");
                    Optional.ofNullable(currentUser.getProfile())
                            .ifPresent(content -> {
                                binaryContentValidator.getOrThrow(content.getId());
                                binaryContentStorage.delete(content.getId());
                                binaryContentRepository.delete(content);
                            });

                    // 프로필 저장
                    log.info("사용자 수정 - 신규 프로필 이미지 생성");
                    BinaryContent binaryContent = profileCommand.toEntity();
                    binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(binaryContent.getId(), profileCommand.bytes());

                    currentUser.updateProfile(binaryContent);
                });

        return userMapper.toDto(currentUser);
    }

    @Override
    @Transactional
    public void delete(UserIdRequestDto requestDto) {
        User deleteUser = userValidator.getOrThrow(requestDto.getId());

        log.info("사용자 상태 삭제");
        userStatusRepository.deleteByUserId(requestDto.getId()); // 로그인 상태 삭제

        Optional.ofNullable(deleteUser.getProfile())
                .ifPresent(binaryContent -> {
                    log.info("사용자 프로필 삭제");
                    UUID deletedId = binaryContent.getId();
                    binaryContentStorage.delete(deletedId);
                    binaryContentRepository.delete(binaryContent);
                });

        log.info("사용자 삭제");
        userRepository.delete(deleteUser); // 유저 삭제
    }

    @Override
    @Transactional
    public UserStatusDto updateUserOnlineStatus(UserIdRequestDto requestDto) {
        User user = userValidator.getOrThrow(requestDto.getId());
        UserStatus status = userStatusRepository.findByUserId(user.getId())
                .orElse(UserStatus.create(user));
        status.updateLastAccessAt();
        
        return userStatusMapper.toDto(status);
    }
}
