package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 임베디드 DB로 갈아끼워지는거 방지
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.saveAll(List.of(
                User.create("alice", "alice@test.com", "password1"),
                User.create("bob", "bob@test.com", "password2"),
                User.create("carol", "carol@test.com", "password3")
        ));
    }

    // ========================= existsByUsername =========================
    @Test
    @DisplayName("존재하는 username이면 true를 반환한다.")
    void existsByUsername_Exists_ReturnsTrue() {
        assertThat(userRepository.existsByUsername("alice")).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 username이면 false를 반환한다.")
    void existsByUsername_NotExists_ReturnsFalse() {
        assertThat(userRepository.existsByUsername("nobody")).isFalse();
    }

    // ========================= existsByEmail =========================
    @Test
    @DisplayName("존재하는 email이면 true를 반환한다.")
    void existsByEmail_Exists_ReturnsTrue() {
        assertThat(userRepository.existsByEmail("bob@test.com")).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 email이면 false를 반환한다.")
    void existsByEmail_NotExists_ReturnsFalse() {
        assertThat(userRepository.existsByEmail("nobody@test.com")).isFalse();
    }

    // ========================= findByUsername =========================
    @Test
    @DisplayName("username으로 사용자 조회에 성공한다.")
    void findByUsername_Success() {
        Optional<User> result = userRepository.findByUsername("carol");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("carol@test.com");
    }
}
