package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private static final String FILE_NAME = "user.dir";

    private void fileSave(Map<UUID, User> data) {
        // File I/O를 통해 직렬화해서 파일 생성
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            objectOutputStream.writeObject(data);
            System.out.println("객체 직렬화 변환 및 파일 저장 완료");
        } catch (IOException e) {
            System.out.println("오류 발생");
            throw new RuntimeException(e);
        }
    }

    private Map<UUID, User> load() {
        // File I/O를 해 역직렬화해서 객체 반환
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Map<UUID, User> loadedUser = (Map<UUID, User>) objectInputStream.readObject(); // 파일에서 객체 읽기
            System.out.println("역직렬화 완료" + loadedUser.toString());
            return loadedUser;
        } catch (FileNotFoundException e) {
            return new HashMap<>(); // 값이 없으면 Map 초기화
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("역직렬화 할 클래스파일이 존재하지않습니다.");
        } catch (IOException e) {
            throw new RuntimeException("데이터 파싱에 실패");
        }
    }

    @Override
    public void save(User user) {
        Map<UUID, User> fileDatabase = this.load(); // 파일 로드해서
        fileDatabase.put(user.getId(), user); // 신규 데이터 저장
        this.fileSave(fileDatabase);
    }


    @Override
    public User findById(UUID id) {
        Map<UUID, User> users = this.load();
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return this.load().values().stream().toList();
    }

    @Override
    public void update(UUID id, User user) {
        Map<UUID, User> users = this.load(); // 파일 로드해서
        users.replace(id, user); // 데이터 저장해
        this.fileSave(users); // 데이터 파일로 만들어

    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> users = this.load(); // 파일 로드해서
        users.remove(id);
        fileSave(users);
    }
}
