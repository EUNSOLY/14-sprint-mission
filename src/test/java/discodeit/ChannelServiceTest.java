package discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.data.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.channel.BasicChannelService;
import com.sprint.mission.discodeit.service.user.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

    @Mock
    private ChannelMapper channelMapper;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private BasicChannelService channelService;

    // ========================= create (PUBLIC) =========================
    @Test
    @DisplayName("공개 채널 생성에 성공한다.")
    void createPublicChannel_Success() {
        PublicChannelCreateRequestDto requestDto = new PublicChannelCreateRequestDto("공개채널", "공개채널 설명");
        UUID channelId = UUID.randomUUID();
        Channel channel = Channel.create(ChannelType.PUBLIC, "공개채널", "공개채널 설명");
        ReflectionTestUtils.setField(channel, "id", channelId);

        given(channelRepository.save(any(Channel.class))).willReturn(channel);
        given(messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channelId)).willReturn(Optional.empty());

        // when
        ChannelDto result = channelService.save(requestDto);

        assertThat(result.id()).isEqualTo(channelId);
        assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);
        assertThat(result.name()).isEqualTo("공개채널");
        assertThat(result.participants()).isEmpty();
        assertThat(result.lastMessageAt()).isNull();
        verify(channelRepository).save(any(Channel.class));

    }

    @Test
    @DisplayName("공개 채널 생성에 실패한다.")
    void createPublicChannel_Fail() {
        // 테스트할 예외가 없음
    }

    // ========================= create (PRIVATE) =========================

    @Test
    @DisplayName("비공개 채널 생성에 성공한다.")
    void createPrivateChannel_Success() {
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        PrivateChannelCreateRequestDto requestDto = new PrivateChannelCreateRequestDto(List.of(userId));
        User user = User.create("sol", "sol@test.com", "1234");
        Channel channel = Channel.create(ChannelType.PRIVATE, "", "");
        ReflectionTestUtils.setField(channel, "id", channelId);
        ChannelDto expectedDto = new ChannelDto(channelId, ChannelType.PRIVATE, "", "", List.of(), null); // 필드는 실제 ChannelDto에 맞게

        given(channelRepository.save(any(Channel.class))).willReturn(channel);
        given(userValidator.getOrThrow(userId)).willReturn(user);
        given(channelMapper.toDto(any(Channel.class))).willReturn(expectedDto);

        // When
        ChannelDto result = channelService.save(requestDto);

        // Then
        assertThat(result.id()).isEqualTo(channelId);
        assertThat(result.type()).isEqualTo(ChannelType.PRIVATE);

        verify(channelRepository).save(any(Channel.class));
        verify(readStatusRepository).save(any(ReadStatus.class));
        verify(channelMapper).toDto(any(Channel.class));
    }

    @Test
    @DisplayName("존재하지 않는 유저가 초대된 비공개 채널 생성에 실패한다.")
    void createPrivateChannel_Fail() {
        UUID userId = UUID.randomUUID();
        PrivateChannelCreateRequestDto requestDto = new PrivateChannelCreateRequestDto(List.of(userId));
        given(userValidator.getOrThrow(any())).willThrow(UserNotFoundException.class);

        // 실행 & 검증
        assertThrows(UserNotFoundException.class, () -> channelService.save(requestDto));

        //
        verify(channelRepository, never()).save(any(Channel.class));
        verify(readStatusRepository, never()).save(any(ReadStatus.class));
    }

    // ========================= update =========================

    @Test
    @DisplayName("공개 채널 수정에 성공한다.")
    void update_PublicChannel_Success() {
        UUID channelId = UUID.randomUUID();
        Channel channel = Channel.create(ChannelType.PUBLIC, "공개채널1", "공개채널1 설명");
        ReflectionTestUtils.setField(channel, "id", channelId);
        ChannelDto expectedDto = new ChannelDto(channelId, ChannelType.PRIVATE, "", "", List.of(), null); // 필드는 실제 ChannelDto에 맞게

        ChannelUpdateRequestDto requestDto = new ChannelUpdateRequestDto("수정_공개채널1", "수정공개채널1 설명");
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        channelService.update(ChannelIdRequestDto.from(channelId), requestDto);

        // 호출 카운트
        verify(channelMapper).toDto(channel);
    }

    @Test
    @DisplayName("비공개 채널은 수정에 실패한다.")
    void update_PrivateChannel_Fail() {
        UUID channelId = UUID.randomUUID();
        Channel channel = Channel.create(ChannelType.PRIVATE, "", "");
        ReflectionTestUtils.setField(channel, "id", channelId);
        ChannelUpdateRequestDto requestDto = new ChannelUpdateRequestDto("새 이름", "새 설명");

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        assertThrows(PrivateChannelUpdateNotAllowedException.class,
                () -> channelService.update(ChannelIdRequestDto.from(channelId), requestDto));

        assertThat(channel.getName()).isEqualTo("");
        verify(channelMapper, never()).toDto(any());

    }

    @Test
    @DisplayName("존재하지 않는 채널을 수정하면 실패한다.")
    void update_ChannelNotFound_Fail() {
        UUID channelId = UUID.randomUUID();
        ChannelUpdateRequestDto requestDto = new ChannelUpdateRequestDto("새 이름", "새 설명");

        given(channelRepository.findById(channelId)).willThrow(ChannelNotFoundException.class);

        assertThrows(ChannelNotFoundException.class, () -> channelService.update(ChannelIdRequestDto.from(channelId), requestDto));

        verify(channelMapper, never()).toDto(any());

    }

    // ========================= delete =========================

    @Test
    @DisplayName("채널 삭제에 성공한다.")
    void delete_Success() {
        UUID channelId = UUID.randomUUID();
        Channel channel = Channel.create(ChannelType.PRIVATE, "", "");
        ReflectionTestUtils.setField(channel, "id", channelId);
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        channelService.delete(ChannelIdRequestDto.from(channelId));

        verify(channelRepository).delete(channel);
    }

    @Test
    @DisplayName("존재하지 않는 채널을 삭제하면 실패한다.")
    void delete_ChannelNotFound_Fail() {
        UUID channelId = UUID.randomUUID();

        given(channelRepository.findById(channelId)).willThrow(ChannelNotFoundException.class);

        assertThrows(ChannelNotFoundException.class, () -> channelService.delete(ChannelIdRequestDto.from(channelId)));

        verify(channelRepository, never()).delete(any(Channel.class));
    }

    // ========================= findAllByUserId =========================

    @Test
    @DisplayName("사용자가 참여한 채널 목록 조회에 성공한다.")
    void findAllByUserId_Success() {
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User user = User.create("sol", "sol@test.com", "1234");
        ReflectionTestUtils.setField(user, "id", userId);
        given(userValidator.getOrThrow(userId)).willReturn(user);

        Channel channel = Channel.create(ChannelType.PRIVATE, "", "");
        ReflectionTestUtils.setField(channel, "id", channelId);
        given(channelRepository.findAll()).willReturn(List.of(channel));

        ReadStatus readStatus = ReadStatus.create(user, channel);
        given(readStatusRepository.findByChannelId(channelId)).willReturn(List.of(readStatus));

        ChannelDto expectedDto = new ChannelDto(channelId, ChannelType.PRIVATE, "", "", List.of(), null);
        given(channelMapper.toDto(channel)).willReturn(expectedDto);

        channelService.findAllByUserId(UserIdRequestDto.from(userId));

        verify(channelRepository).findAll();
        verify(readStatusRepository).findByChannelId(channelId);
        verify(channelMapper).toDto(channel);
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 채널 목록 조회에 실패한다.")
    void findAllByUserId_UserNotFound_Fail() {
        // Given : 사전 준비
        UUID userId = UUID.randomUUID();
        given(channelRepository.findAll()).willReturn(List.of(Channel.create(ChannelType.PRIVATE, "", "")));
        given(userValidator.getOrThrow(userId)).willThrow(UserNotFoundException.class);

        // 실행 + 검증
        assertThrows(UserNotFoundException.class, () -> channelService.findAllByUserId(UserIdRequestDto.from(userId)));

        // 체크
        verify(channelRepository).findAll();
        verify(readStatusRepository, never()).findByChannelId(any());
    }
}
