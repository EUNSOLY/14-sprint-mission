package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private static final String FILE_NAME = "message.dir";
    private final FileUserService userService;
    private final FileChannelService channelService;

    public FileMessageService(
            FileUserService userService,
            FileChannelService channelService
    ) {
        this.channelService = channelService;
        this.userService = userService;
    }

    private void fileSave(Map<UUID, Message> data) {
        // File I/O를 통해 직렬화해서 파일 생성
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            objectOutputStream.writeObject(data);
            System.out.println("객체 직렬화 변환 및 파일 저장 완료");
        } catch (IOException e) {
            throw new RuntimeException("IOException오류 발생 : ", e);
        }
    }

    private Map<UUID, Message> load() {
        // File I/O를 해 역직렬화해서 객체 반환
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Map<UUID, Message> loadedMessage = (Map<UUID, Message>) objectInputStream.readObject(); // 파일에서 객체 읽기
            System.out.println("역직렬화 완료" + loadedMessage.toString());
            return loadedMessage;

        } catch (FileNotFoundException e) {
            return new HashMap<>(); // 값이 없으면 Map 초기화
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("역직렬화 할 클래스파일이 존재하지않습니다.", e);
        } catch (IOException e) {
            throw new RuntimeException("데이터 파싱에 실패", e);
        }
    }

    @Override
    public void save(Message message) {
        Map<UUID, Message> fileDatabase = this.load(); // 파일 로드해서
        if (Objects.isNull(this.userService.find(message.getUserId()))) {
            throw new RuntimeException("잘못된 사용자 ID 입니다. 확인 해주세요. 메세지 내용 : " + message.getMessage());
        }

        if (Objects.isNull(this.channelService.find(message.getChannelId()))) {
            throw new RuntimeException("잘못된 사용자 ID 입니다. 확인 해주세요. 메세지 내용 : " + message.getMessage());
        }

        fileDatabase.put(message.getId(), message); // 신규 데이터 저장
        this.fileSave(fileDatabase);
    }

    @Override
    public Message find(UUID id) {
        Map<UUID, Message> messages = this.load();
        if (!messages.containsKey(id)) {
            return null;
        }
        return messages.get(id);
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        Map<UUID, Message> messages = this.load();
        return messages.values().stream()
                .filter(message -> message.getUserId().equals(userId))
                .toList();

    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        Map<UUID, Message> messages = this.load();
        return messages.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();


    }

    @Override
    public List<Message> findByChannelIdAndUserId(UUID userId, UUID channelId) {
        Map<UUID, Message> messages = this.load();
        return messages.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .filter(message -> message.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<Message> findAll() {
        return this.load().values().stream().toList();
    }

    @Override
    public void update(UUID id, Message message) {
        Map<UUID, Message> messages = this.load(); // 파일 로드해서
        if (!messages.containsKey(id)) {
            throw new RuntimeException("요청한 사용자의 데이터가 존재하지 않습니다.");
        }

        Map<UUID, Message> memoryDatabase = new HashMap<>(messages); // 파일을 덮어 씌우고
        memoryDatabase.replace(id, message); // 데이터 저장해
        this.fileSave(memoryDatabase); // 데이터 파일로 만들어

    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> messages = this.load(); // 파일 로드해서
        if (!messages.containsKey(id)) {
            throw new RuntimeException("요청한 사용자의 데이터가 존재하지 않습니다.");
        }
        messages.remove(id);
        fileSave(messages);
    }
}
