package com.sprint.mission.discodeit.repository.file;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class FileAbstractRepository {
    private final String fileName;

    protected FileAbstractRepository(String fileName) {
        this.fileName = fileName;
    }

    protected <T> void fileSave(Map<UUID, T> data) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(this.fileName))) {
            objectOutputStream.writeObject(data);
            System.out.println("객체 직렬화 변환 및 파일 저장 완료");
        } catch (IOException e) {
            System.out.println("오류 발생");
            throw new RuntimeException(e);
        }
    }

    protected <T> Map<UUID, T> load() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(this.fileName))) {
            Map<UUID, T> loadedMessage = (Map<UUID, T>) objectInputStream.readObject(); // 파일에서 객체 읽기
            System.out.println("역직렬화 완료" + loadedMessage.toString());
            return loadedMessage;

        } catch (FileNotFoundException e) {
            return new HashMap<>(); // 값이 없으면 Map 초기화
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("역직렬화 할 클래스파일이 존재하지않습니다.");
        } catch (IOException e) {
            throw new RuntimeException("데이터 파싱에 실패", e);
        }
    }
}
