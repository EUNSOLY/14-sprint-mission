package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.entity.user.Role;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class AdminInitializer implements ApplicationRunner {

    private final String ADMIN_USERNAME;
    private final String ADMIN_EMAIL;
    private final String ADMIN_PWD;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AdminInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${discodeit.security.admin-username}") String adminUsername,
            @Value("${discodeit.security.admin-email}") String adminEmail,
            @Value("${discodeit.security.admin-pwd}") String adminPwd
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ADMIN_USERNAME = adminUsername;
        this.ADMIN_EMAIL = adminEmail;
        this.ADMIN_PWD = adminPwd;
    }


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("관리자 아이디 생성 run 진입");

        if (userRepository.existsByUsername(this.ADMIN_USERNAME)) {
            log.info("이미 존재하는 username으로 관리자를 생성하지 않습니다.");
            return;
        }

        User user = User.create(this.ADMIN_USERNAME, this.ADMIN_EMAIL, passwordEncoder.encode(this.ADMIN_PWD));
        user.updateRole(Role.ADMIN);

        userRepository.save(user);
    }
}
