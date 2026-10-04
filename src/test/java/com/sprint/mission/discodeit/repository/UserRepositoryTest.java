package com.sprint.mission.discodeit.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@DataJpaTest    // 기본적으로 @Transactional을 활성화하
@Transactional  // 명시적으로 표시
@EnableJpaAuditing
@ActiveProfiles(value = "test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        log.info("테스트 Before");
    }

    @Test
    void temp() {
        // given
        log.info("xptmxm");
    }
}