package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User find(UUID id) {
        User findUser = userRepository.findById(id);
        if (Objects.isNull(findUser)) {
            throw new RuntimeException("찾으시는 회원이 존재하지 않습니다.");
        }

        return findUser;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void update(UUID id, User user) {
        User findUser = userRepository.findById(id);
        if (Objects.isNull(findUser)) {
            throw new RuntimeException("수정 할 회원이 존재하지 않습니다.");
        }
        userRepository.update(id, user);
    }

    @Override
    public void delete(UUID id) {
        User findUser = userRepository.findById(id);
        if (Objects.isNull(findUser)) {
            throw new RuntimeException("삭제 할 회원이 존재하지 않습니다.");
        }
        userRepository.delete(id);
    }
}
