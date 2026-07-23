package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    private static final String FILE_NAME = "message.dir";

    private void fileSave(Map<UUID, Message> data) {
        // File I/O를 통해 직렬화해서 파일 생성
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            objectOutputStream.writeObject(data);
            System.out.println("객체 직렬화 변환 및 파일 저장 완료");
        } catch (IOException e) {
            System.out.println("오류 발생");
            throw new RuntimeException(e);
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
            throw new RuntimeException("역직렬화 할 클래스파일이 존재하지않습니다.");
        } catch (IOException e) {
            throw new RuntimeException("데이터 파싱에 실패");
        }
    }

    @Override
    public void save(Message message) {
        Map<UUID, Message> fileDatabase = this.load(); // 파일 로드해서
        fileDatabase.put(message.getId(), message); // 신규 데이터 저장
        this.fileSave(fileDatabase);
    }


    @Override
    public Message findById(UUID id) {
        Map<UUID, Message> messages = this.load();
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
    public List<Message> findByChannelIdAndUserId(UUID channelId, UUID userId) {
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
        messages.replace(id, message); // 데이터 저장해
        this.fileSave(messages); // 데이터 파일로 만들어

    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> messages = this.load(); // 파일 로드해서
        messages.remove(id);
        fileSave(messages);
    }
}
