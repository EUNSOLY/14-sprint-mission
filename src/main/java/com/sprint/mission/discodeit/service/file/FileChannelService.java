package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private static final String FILE_NAME = "channel.dir";

    private void fileSave(Map<UUID, Channel> data) {
        // File I/O를 통해 직렬화해서 파일 생성
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            objectOutputStream.writeObject(data);
            System.out.println("객체 직렬화 변환 및 파일 저장 완료");
        } catch (IOException e) {
            throw new RuntimeException("IOException오류 발생 : ", e);
        }
    }

    private Map<UUID, Channel> load() {
        // File I/O를 해 역직렬화해서 객체 반환
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Map<UUID, Channel> loadedChannel = (Map<UUID, Channel>) objectInputStream.readObject(); // 파일에서 객체 읽기
            System.out.println("역직렬화 완료" + loadedChannel.toString());
            return loadedChannel;

        } catch (FileNotFoundException e) {
            return new HashMap<>(); // 값이 없으면 Map 초기화
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("역직렬화 할 클래스파일이 존재하지않습니다.", e);
        } catch (IOException e) {
            throw new RuntimeException("데이터 파싱에 실패", e);
        }
    }

    @Override
    public void save(Channel channel) {
        Map<UUID, Channel> fileDatabase = this.load(); // 파일 로드해서
        fileDatabase.put(channel.getId(), channel); // 신규 데이터 저장
        this.fileSave(fileDatabase);
    }

    @Override
    public Channel find(UUID id) {
        Map<UUID, Channel> Channels = this.load();
        if (!Channels.containsKey(id)) {
            return null;
        }
        return Channels.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return this.load().values().stream().toList();
    }

    @Override
    public void update(UUID id, Channel channel) {
        Map<UUID, Channel> Channels = this.load(); // 파일 로드해서
        if (!Channels.containsKey(id)) {
            throw new RuntimeException("요청한 사용자의 데이터가 존재하지 않습니다.");
        }

        Map<UUID, Channel> memoryDatabase = new HashMap<>(Channels); // 파일을 덮어 씌우고
        memoryDatabase.replace(id, channel); // 데이터 저장해
        this.fileSave(memoryDatabase); // 데이터 파일로 만들어

    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> Channels = this.load(); // 파일 로드해서
        if (!Channels.containsKey(id)) {
            throw new RuntimeException("요청한 사용자의 데이터가 존재하지 않습니다.");
        }
        Channels.remove(id);
        fileSave(Channels);
    }
}
