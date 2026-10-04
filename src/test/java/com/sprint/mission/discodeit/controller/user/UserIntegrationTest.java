package com.sprint.mission.discodeit.controller.user;


import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserIdRequestDto;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.user.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserStatusRepository userStatusRepository;

    @Test
    @DisplayName("사용자 생성")
    void create_user_success() {
        // given
        UserCreateRequest request = new UserCreateRequest("sol", "sol@test.com", "1234");

        // when
        User saved = userService.save(request, null);
        entityManager.flush(); // 영속성 컨텍스트의 변경을 DB에 반영(INSERT 실행). 이때 @CreationTimestamp로 createdAt이 채워짐
        entityManager.clear(); // 영속성 컨텍스트 비움 → 이후 조회는 DB에서 새로 읽음

        // then: DB 조회해서 확인
        User found = userRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getUsername()).isEqualTo("sol");
        assertThat(found.getEmail()).isEqualTo("sol@test.com");
        assertThat(found.getCreatedAt()).isNotNull();
    }


    @Test
    @DisplayName("사용자 삭제")
    void delete_user_success() {
        // given: 삭제할 사용자를 실제로 생성 (서비스가 UserStatus도 함께 만듦)
        User saved = userService.save(new UserCreateRequest("sol", "sol@test.com", "1234"), null);
        UUID userId = saved.getId();
        entityManager.flush();
        entityManager.clear();

        // when
        userService.delete(UserIdRequestDto.from(userId));
        entityManager.flush();
        entityManager.clear();

        // then : DB 체크
        assertThat(userRepository.findById(userId)).isEmpty();
        assertThat(userStatusRepository.findByUserId(userId)).isEmpty();
    }

}
