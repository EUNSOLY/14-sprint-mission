package discodeit;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserDuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.UserDuplicateNameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentValidator;
import com.sprint.mission.discodeit.service.user.BasicUserService;
import com.sprint.mission.discodeit.service.user.UserValidator;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private BinaryContentValidator binaryContentValidator;
    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicUserService userService;

    // ==================== create ====================
    // 생성 성공 케이스 2건
    @Test
    @DisplayName("프로필 없이 사용자 생성에 성공한다")
    void create_OutProfile_Success() {

        // Given : 사전준비
        UserCreateRequest request = new UserCreateRequest("sol", "sol@test.com", "1234");
        given(userRepository.existsByUsername("sol")).willReturn(false);       // username 중복 검사 시 "중복 없음(false)" 반환
        given(userRepository.existsByEmail("sol@test.com")).willReturn(false); // email 중복 검사 시 "중복 없음(false)" 반환

        // When : 메서드 실행
        User result = userService.save(request, null);

        // Then : 검증
        assertThat(result.getUsername()).isEqualTo("sol");       // 반환된 User의 username이 요청값과 같은지
        assertThat(result.getEmail()).isEqualTo("sol@test.com"); // 반환된 User의 email이 요청값과 같은지
        assertThat(result.getProfile()).isNull();                         // 프로필을 넘기지 않았으므로 profile은 null이어야 함
        verify(userRepository).save(any(User.class));                     // userRepository.save()가 User 타입 인자로 1번 호출됐는지
        verify(userStatusRepository).save(any(UserStatus.class));         // 사용자 생성 시 UserStatus도 함께 저장됐는지 (1번 호출)
        verify(binaryContentRepository, never()).save(any());             // 프로필이 없으므로 binaryContentRepository.save()는 한 번도 호출되지 않아야 함
    }

    @Test
    @DisplayName("프로필과 함께 사용자 생성에 성공하면 프로필 파일이 저장된다.")
    void create_Profile_Success() {
        // Given : 사전준비
        UserCreateRequest request = new UserCreateRequest("sol", "sol@test.com", "1234");
        BinaryContentCreateRequestDto profileRequest = new BinaryContentCreateRequestDto("profile.png", "image/png", new byte[]{1, 2, 3});
        given(userRepository.existsByUsername("sol")).willReturn(false);       // username 중복 검사 시 "중복 없음(false)" 반환
        given(userRepository.existsByEmail("sol@test.com")).willReturn(false); // email 중복 검사 시 "중복 없음(false)" 반환
        // BinaryContent 타입으로 들어오면 첫번째 값을반환해라
        given(binaryContentRepository.save(any(BinaryContent.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        User result = userService.save(request, profileRequest);

        // Then
        assertThat(result.getProfile()).isNotNull();
        assertThat(result.getProfile().getFileName()).isEqualTo("profile.png");
        verify(binaryContentStorage).put(any(), any(byte[].class));
        verify(userRepository).save(any(User.class));
    }

    // 생성 실패 케이스 2건
    @Test
    @DisplayName("이미 존재하는 사용자 이름이면 생성에 실패한다.")
    void create_duplicateUsername_Fail() {
        // Given : 사전준비
        UserCreateRequest request = new UserCreateRequest("sol", "sol@test.com", "1234");
        given(userRepository.existsByUsername("sol")).willReturn(true);       // username 중복 검사 시 "중복 있음(true)" 반환

        // When & Then
        assertThrows(UserDuplicateNameException.class, () -> userService.save(request, null));

        verify(userRepository, never()).save(any(User.class));
        verify(userStatusRepository, never()).save(any(UserStatus.class));
    }

    @Test
    @DisplayName("이미 존재하는 사용자 이메일이면 생성에 실패한다.")
    void create_duplicateUserEmail_Fail() {
        // Given : 사전준비
        UserCreateRequest request = new UserCreateRequest("sol", "sol@test.com", "1234");
        given(userRepository.existsByEmail("sol@test.com")).willReturn(true);

        // When & Then
        assertThrows(UserDuplicateEmailException.class, () -> userService.save(request, null));

        verify(userRepository, never()).save(any(User.class));
        verify(userStatusRepository, never()).save(any(UserStatus.class));
    }

    // ==================== update ====================

    // 수정 성공 케이스 2건
    @Test
    @DisplayName("이름, 이메일, 비밀번호 수정에 성공한다.")
    void update_Success() {
        // Given
        UUID id = UUID.randomUUID();
        User user = User.create("sol", "sol@test.com", "1234");
        UserUpdateRequest request = new UserUpdateRequest("sol2", "sol2@test.com", "1234");
        given(userValidator.getOrThrow(id)).willReturn(user);
        given(userRepository.existsByUsername("sol2")).willReturn(false);
        given(userRepository.existsByEmail("sol2@test.com")).willReturn(false);

        // When
        userService.update(UserIdRequestDto.from(id), request, null);

        // Then : 검증
        assertThat(user.getUsername()).isEqualTo("sol2");
        assertThat(user.getEmail()).isEqualTo("sol2@test.com");
        assertThat(user.getProfile()).isNull();

        verify(userMapper).toDto(user);

    }

    @Test
    @DisplayName("새로운 프로필이 들어오면 기존 프로필을 삭제하고 새로운 프로필로 교체한다.")
    void update_newProfile_Success() {
        // Given
        UUID oldUserId = UUID.randomUUID();
        UUID oldProfileId = UUID.randomUUID();

        User user = User.create("sol", "sol@test.com", "1234");
        BinaryContent oldProfile = BinaryContent.create("old.png", "image/png", 1L);
        ReflectionTestUtils.setField(oldProfile, "id", oldProfileId);
        user.updateProfile(oldProfile);

        UserUpdateRequest updatedUserRequest = new UserUpdateRequest("sol2", "sol2@test.com", "1234");
        BinaryContentCreateRequestDto updatedProfileRequest = new BinaryContentCreateRequestDto("new.png", "image/png", new byte[]{1, 2});

        given(userValidator.getOrThrow(oldUserId)).willReturn(user);
        given(binaryContentValidator.getOrThrow(oldProfileId)).willReturn(oldProfile);

        // When
        userService.update(UserIdRequestDto.from(oldUserId), updatedUserRequest, updatedProfileRequest);

        // Then
        verify(binaryContentStorage).delete(oldProfileId);
        verify(binaryContentRepository).delete(oldProfile);
        verify(binaryContentRepository).save(any(BinaryContent.class));
        assertThat(user.getProfile().getFileName()).isEqualTo("new.png");
    }

    // 수정 실패 케이스 2건
    @Test
    @DisplayName("존재하지 않는 사용자를 수정하려고하면 실패한다.")
    void update_notUser_Fail() {
        // Given : 사전준비
        UUID userId = UUID.randomUUID();
        UserUpdateRequest updateRequest = new UserUpdateRequest("newSol", "newSol@test.com", "1234");
        given(userValidator.getOrThrow(userId)).willThrow(new UserNotFoundException(Map.of("수정 유저 ID", userId)));

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.update(UserIdRequestDto.from(userId), updateRequest, null));
    }

    // 수정 실패 케이스 2건
    @Test
    @DisplayName("이미 존재하는 이름으로 변경을 요청하면 실패한다.")
    void update_duplicateUsername_Fail() {
        UUID userId = UUID.randomUUID();
        User user = User.create("sol", "sol@test.com", "1234");
        UserUpdateRequest updateRequest = new UserUpdateRequest("newSol", "newSol@test.com", "1234");

        given(userValidator.getOrThrow(userId)).willReturn(user);
        given(userRepository.existsByUsername("newSol")).willReturn(true);

        assertThrows(UserDuplicateNameException.class, () -> userService.update(UserIdRequestDto.from(userId), updateRequest, null));

    }

    // ==================== delete ====================

    // 삭제 성공 2건
    @Test
    @DisplayName("프로필 없는 사용자 삭제 성공한다.")
    void delete_notProfile_Success() {
        UUID userId = UUID.randomUUID();
        User user = User.create("sol", "sol@test.com", "1234");
        given(userValidator.getOrThrow(userId)).willReturn(user);

        userService.delete(UserIdRequestDto.from(userId));

        verify(userStatusRepository).deleteByUserId(userId);
        verify(binaryContentStorage, never()).delete(any());
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("프로필이 있는 사용자를 삭제하면 프로필도 함께 삭제된다.")
    void delete_profile_Success() {
        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        User user = User.create("sol", "sol@test.com", "1234");
        BinaryContent profile = BinaryContent.create("old.png", "image/png", 1L);

        ReflectionTestUtils.setField(profile, "id", profileId);
        user.updateProfile(profile);
        given(userValidator.getOrThrow(userId)).willReturn(user);

        userService.delete(UserIdRequestDto.from(userId));

        verify(userStatusRepository).deleteByUserId(userId);
        verify(binaryContentStorage).delete(any());
        verify(userRepository).delete(user);
    }


    // 삭제 실패 1건
    @Test
    @DisplayName("존재하지않는 사용자를 삭제하면 삭제에 실패한다.")
    void delete_UserNotFound_Fail() {
        UUID userId = UUID.randomUUID();
        given(userValidator.getOrThrow(userId)).willThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> userService.delete(UserIdRequestDto.from(userId)));
    }
}
