package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.FileStorageUtil;
import com.sprint.mission.discodeit.common.validator.BinaryContentValidator;
import com.sprint.mission.discodeit.common.validator.UserValidator;
import com.sprint.mission.discodeit.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequestDto;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final FileStorageUtil fileStorageUtil;
    private final UserValidator userValidator;
    private final BinaryContentValidator binaryContentValidator;

    @Override
    public void save(UserCreateRequestDto requestDto) {
        List<User> users = userRepository.findAll();
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

        if (Objects.nonNull(requestDto.getProfile())) {
            String imagePath = fileStorageUtil.imageUpload(requestDto.getProfile().getFileName(), requestDto.getProfile().getBytes());
            BinaryContent binaryContent = requestDto.getProfile().toEntity(imagePath); // User 프로필 파일 저장 Entity 생성
            binaryContentRepository.save(binaryContent); // BinaryContent 저장
            savedUser.updateProfile(binaryContent.getId()); // 프로필 Entity 연계
        }
        userRepository.save(savedUser); // 저장
    }

    @Override
    public UserResponseDto find(UserIdRequestDto requestDto) {
        User currentUser = userValidator.getOrThrow(requestDto.getId());
        boolean userStatus = userStatusRepository.findByUserId(currentUser.getId())
                .map(UserStatus::isCurrentlyLoggedIn)
                .orElse(false);

        UserStatusType userStatusType = userStatus ? UserStatusType.ONLINE : UserStatusType.OFFLINE;

        String profileImagePath = null;
        if (Objects.nonNull(currentUser.getProfileId())) {
            BinaryContent binaryContent = binaryContentValidator.getOrThrow(currentUser.getProfileId());
            profileImagePath = binaryContent.getPath();
        }

        return UserResponseDto.from(currentUser, userStatusType, profileImagePath);
    }

    // Stream 고민해보기 (메서드 분할 고민해보깅)
    @Override
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    boolean userStatus = userStatusRepository.findByUserId(user.getId())
                            .map(UserStatus::isCurrentlyLoggedIn)
                            .orElse(false);

                    UserStatusType userStatusType = userStatus ? UserStatusType.ONLINE : UserStatusType.OFFLINE;
                    String profileImagePath = null;
                    if (Objects.nonNull(user.getProfileId())) {
                        BinaryContent binaryContent = binaryContentValidator.getOrThrow(user.getProfileId());
                        profileImagePath = binaryContent.getPath();
                    }
                    return UserResponseDto.from(user, userStatusType, profileImagePath);
                })
                .toList();
    }

    @Override
    public void update(UserUpdateRequestDto updateRequestDto) {
        User currentUser = userValidator.getOrThrow(updateRequestDto.getId());

        currentUser.update(updateRequestDto.getName(), updateRequestDto.getEmail(), updateRequestDto.getPassword());

        // 새로운 프로필 데이터가 들어오면 기존 프로필 데이터 삭제 -> 신규 프로필 저장 -> User 엔티티 연계
        if (Objects.nonNull(updateRequestDto.getProfile())) {
            String imagePath = fileStorageUtil.imageUpload(updateRequestDto.getProfile().getFileName(), updateRequestDto.getProfile().getBytes());
            if (Objects.nonNull(currentUser.getProfileId())) {
                BinaryContent originImage = binaryContentValidator.getOrThrow(currentUser.getProfileId());
                binaryContentRepository.delete(currentUser.getProfileId()); // 기존 프로필 데이터 삭제
                fileStorageUtil.deleteUploadImage(originImage.getPath());
            }
            BinaryContent newProfile = updateRequestDto.getProfile().toEntity(imagePath); // User 프로필 파일 저장 Entity 생성
            binaryContentRepository.save(newProfile); // BinaryContent 저장
            currentUser.updateProfile(newProfile.getId()); // 프로필 Entity 연계
        }

        userRepository.update(currentUser.getId(), currentUser);
    }

    @Override
    public void delete(UserIdRequestDto requestDto) {
        User deleteUser = userValidator.getOrThrow(requestDto.getId());


        userStatusRepository.deleteByUserId(requestDto.getId()); // 로그인 상태 삭제
        if (Objects.nonNull(deleteUser.getProfileId())) {
            BinaryContent originImage = binaryContentValidator.getOrThrow(deleteUser.getProfileId());
            binaryContentRepository.delete(deleteUser.getProfileId()); // 프로필 파일 삭제
            fileStorageUtil.deleteUploadImage(originImage.getPath());
        }

        userRepository.delete(requestDto.getId()); // 유저 삭제
    }
}
