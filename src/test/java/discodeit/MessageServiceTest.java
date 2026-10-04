package discodeit;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageIdRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.message.data.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.channel.ChannelValidator;
import com.sprint.mission.discodeit.service.message.BasicMessageService;
import com.sprint.mission.discodeit.service.user.UserValidator;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private UserValidator userValidator;

    @Mock
    private ChannelValidator channelValidator;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @Spy
    private PageResponseMapper pageResponseMapper = new PageResponseMapper();

    @InjectMocks
    private BasicMessageService messageService;


    // ========================= create =========================
    @Test
    @DisplayName("첨부파일 없이 메세지 생성에 성공한다.")
    void createMessage_Success() {
        // Given : 사전 준비
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MessageCreateRequestDto requestDto = new MessageCreateRequestDto("콘텐츠 내용 블라블라", channelId, userId);
        User user = User.create("쏠", "sol@test.com", "1234");
        ReflectionTestUtils.setField(user, "id", userId);

        Channel channel = Channel.create(ChannelType.PUBLIC, "공개채널1", "공개채널 설명");
        ReflectionTestUtils.setField(channel, "id", channelId);

        Message message = Message.create("콘텐츠 내용 블라블라", user, channel);
        given(userValidator.getOrThrow(userId)).willReturn(user);
        given(channelValidator.getOrThrow(channelId)).willReturn(channel);
        given(messageRepository.save(any())).willReturn(message);
        given(messageMapper.toDto(any())).willReturn(MessageDto.to(message, null, null, null));


        MessageDto result = messageService.save(requestDto, null);

        assertThat(result.content()).isEqualTo("콘텐츠 내용 블라블라");
        verify(binaryContentRepository, never()).save(any());
        verify(binaryContentStorage, never()).put(any(), any());

    }

    @Test
    @DisplayName("첨부파일과 함께 메세지 생성에 성공한다.")
    void createMessageWithAttachments_Success() {
        // Given
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MessageCreateRequestDto requestDto = new MessageCreateRequestDto("첨부파일 메세지", channelId, userId);
        User user = User.create("쏠", "sol@test.com", "1234");
        ReflectionTestUtils.setField(user, "id", userId);

        Channel channel = Channel.create(ChannelType.PUBLIC, "공개채널1", "공개채널 설명");
        ReflectionTestUtils.setField(channel, "id", channelId);

        byte[] bytes1 = "file1".getBytes();
        byte[] bytes2 = "file2".getBytes();
        List<BinaryContentCreateRequestDto> attachments = List.of(
                new BinaryContentCreateRequestDto("a.png", "image/png", bytes1),
                new BinaryContentCreateRequestDto("b.png", "image/png", bytes2)
        );

        UUID contentId1 = UUID.randomUUID();
        UUID contentId2 = UUID.randomUUID();

        BinaryContent content1 = BinaryContent.create("a.png", "image/png", (long) bytes1.length);
        BinaryContent content2 = BinaryContent.create("b.png", "image/png", (long) bytes2.length);
        ReflectionTestUtils.setField(content1, "id", contentId1);
        ReflectionTestUtils.setField(content2, "id", contentId2);

        given(userValidator.getOrThrow(userId)).willReturn(user);
        given(channelValidator.getOrThrow(channelId)).willReturn(channel);
        given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(content1, content2);
        given(messageMapper.toDto(any())).willAnswer(invocation ->
                MessageDto.to(invocation.getArgument(0), null, null, null));

        // When
        MessageDto result = messageService.save(requestDto, attachments);

        // Then
        assertThat(result.content()).isEqualTo("첨부파일 메세지");
        verify(binaryContentRepository, times(2)).save(any(BinaryContent.class));
        verify(binaryContentStorage).put(contentId1, bytes1);
        verify(binaryContentStorage).put(contentId2, bytes2);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

        verify(messageRepository).save(captor.capture());
        assertThat(captor.getValue().getAttachments()).containsExactly(content1, content2);
    }

    @Test
    @DisplayName("존재하지 않는 작성자로 메세지 생성 시 실패한다.")
    void createMessage_UserNotFound_Fail() {
        // Given
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MessageCreateRequestDto requestDto = new MessageCreateRequestDto("첨부파일 메세지", channelId, userId);

        given(userValidator.getOrThrow(userId)).willThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> messageService.save(requestDto, null));

        verify(channelValidator, never()).getOrThrow(any());
        verify(messageRepository, never()).save(any());
        verify(messageMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("존재하지 않는 채널로 메세지 생성 시 실패한다.")
    void createMessage_ChannelNotFound_Fail() {
        // Given
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MessageCreateRequestDto requestDto = new MessageCreateRequestDto("첨부파일 메세지", channelId, userId);
        User user = User.create("쏠", "sol@test.com", "1234");


        given(userValidator.getOrThrow(userId)).willReturn(user);
        given(channelValidator.getOrThrow(channelId)).willThrow(ChannelNotFoundException.class);

        assertThrows(ChannelNotFoundException.class, () -> messageService.save(requestDto, null));

        verify(userValidator).getOrThrow(any());
        verify(channelValidator).getOrThrow(any());
        verify(messageRepository, never()).save(any());
        verify(messageMapper, never()).toDto(any());
    }

    // ========================= update =========================
    @Test
    @DisplayName("메세지 내용 수정에 성공한다.")
    void updateMessage_Success() {
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID();

        MessageUpdateRequestDto requestDto = new MessageUpdateRequestDto("메세지 수정");
        User user = User.create("쏠", "sol@test.com", "1234");
        ReflectionTestUtils.setField(user, "id", userId);
        Channel channel = Channel.create(ChannelType.PUBLIC, "공개채널1", "공개채널 설명");
        ReflectionTestUtils.setField(channel, "id", channelId);
        Message message = Message.create("메세지", user, channel);

        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

        messageService.update(MessageIdRequestDto.from(messageId), requestDto);

        assertThat(message.getContent()).isEqualTo("메세지 수정");
        verify(messageMapper).toDto(message);
    }

    @Test
    @DisplayName("존재하지 않는 메세지 수정 시 실패한다.")
    void updateMessage_NotFound_Fail() {
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequestDto requestDto = new MessageUpdateRequestDto("메세지 수정");
        given(messageRepository.findById(messageId)).willThrow(MessageNotFoundException.class);

        assertThrows(MessageNotFoundException.class, () -> messageService.update(MessageIdRequestDto.from(messageId), requestDto));

        verify(messageMapper, never()).toDto(any());
    }

    // ========================= delete =========================
    @Test
    @DisplayName("메세지 삭제에 성공한다.")
    void deleteMessage_Success() {
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID();

        MessageUpdateRequestDto requestDto = new MessageUpdateRequestDto("메세지 수정");
        User user = User.create("쏠", "sol@test.com", "1234");
        ReflectionTestUtils.setField(user, "id", userId);
        Channel channel = Channel.create(ChannelType.PUBLIC, "공개채널1", "공개채널 설명");
        ReflectionTestUtils.setField(channel, "id", channelId);
        Message message = Message.create("메세지", user, channel);

        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

        messageService.delete(MessageIdRequestDto.from(messageId));

        verify(binaryContentRepository, never()).deleteAll();
        verify(binaryContentStorage, never()).delete(any());
        verify(messageRepository).delete(any());
    }

    @Test
    @DisplayName("첨부파일이 있는 메세지 삭제 시 첨부파일도 함께 삭제된다.")
    void deleteMessageWithAttachments_Success() {
        // Given
        UUID messageId = UUID.randomUUID();
        Message message = Message.create("", null, null);
        List<BinaryContent> attachments = List.of(
                BinaryContent.create("a.png", "image/png", 1L),
                BinaryContent.create("a.png", "image/png", 1L)
        );
        ReflectionTestUtils.setField(message, "attachments", attachments);

        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

        messageService.delete(MessageIdRequestDto.from(messageId));

        verify(binaryContentStorage, times(attachments.size())).delete(any());
        verify(binaryContentRepository).deleteAll(any());
        verify(messageRepository).delete(any());
    }

    @Test
    @DisplayName("존재하지 않는 메세지 삭제 시 실패한다.")
    void deleteMessage_NotFound_Fail() {
        UUID messageId = UUID.randomUUID();

        given(messageRepository.findById(messageId)).willThrow(MessageNotFoundException.class);

        assertThrows(MessageNotFoundException.class, () -> messageService.delete(MessageIdRequestDto.from(messageId)));
    }

    // ========================= findAllByChannelId =========================
    @Test
    @DisplayName("커서 없이 채널의 메세지 첫 페이지 조회에 성공한다.")
    void findAllByChannelId_FirstPage_Success() {
        // Given
        UUID channelId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 1);
        Message message = Message.create("메세지1", null, null);
        ReflectionTestUtils.setField(message, "createdAt", Instant.parse("2025-01-01T00:00:00Z"));
        Slice<Message> slice = new SliceImpl<>(List.of(message), pageable, true);

        given(messageRepository.findFirstPage(channelId, pageable)).willReturn(slice);

        // When
        PageResponse<MessageDto> result = messageService.findAllByChannelId(ChannelIdRequestDto.from(channelId), pageable, null);

        // Then
        assertThat(result.content()).hasSize(1);
        assertThat(result.hasNext()).isTrue();
        assertThat(result.nextCursor()).isEqualTo("2025-01-01T00:00:00Z");
        verify(channelValidator).getOrThrow(channelId);
        verify(messageRepository).findFirstPage(channelId, pageable);
        verify(messageRepository, never()).findByChannelId(any(), any(Instant.class), any());
    }

    @Test
    @DisplayName("존재하지 않는 채널의 메세지 조회 시 실패한다.")
    void findAllByChannelId_ChannelNotFound_Fail() {
        // Given
        UUID channelId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 2);

        given(channelValidator.getOrThrow(channelId)).willThrow(ChannelNotFoundException.class);

        // When & Then
        assertThrows(ChannelNotFoundException.class,
                () -> messageService.findAllByChannelId(ChannelIdRequestDto.from(channelId), pageable, null));

        verify(messageRepository, never()).findFirstPage(any(), any());
        verify(messageRepository, never()).findByChannelId(any(), any(Instant.class), any());
    }

}
