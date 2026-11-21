package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Slf4j
@Configuration
@EnableMethodSecurity
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final CsrfTokenRequestHandler spaCsrfTokenRequestHandler;
    private final AuthenticationSuccessHandler loginSuccessHandler;
    private final AuthenticationFailureHandler loginFailureHandler;
    private final LogoutSuccessHandler httpStatusReturningLogoutSuccessHandler;
    private final AccessDeniedHandler discodeitAccessDeniedHandler;

    @Value("${discodeit.security.remember-me.secret-key}")
    private String rememberMeKey;


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SessionRegistry sessionRegistry,
            UserDetailsService userDetailsService
    ) throws Exception {

        http.csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(spaCsrfTokenRequestHandler)
        );


//        http.formLogin(Customizer.withDefaults());
        http.formLogin(login -> login
                .loginProcessingUrl("/api/auth/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
        );

        http.logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(httpStatusReturningLogoutSuccessHandler)
        );

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/csrf-token").permitAll()         // csrf 토큰 발급
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()  // 회원가입
                .requestMatchers("/api/auth/login").permitAll()              // 로그인 요청
                .requestMatchers("/api/auth/logout").permitAll()             // 로그아웃 요청
                .requestMatchers("/", "/index.html", "/favicon.ico", "/assets/**").permitAll()        // React 정적 리소스
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()   // Swagger
                .requestMatchers("/actuator/**").permitAll()                                                // Actuator
                .anyRequest().authenticated()                                                                 // 외 모든 요청은 인증 필수
        );

        // 인증 X : 401 / 권한이 없는 경우 403 응답을 반환
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .accessDeniedHandler(discodeitAccessDeniedHandler)
        );

        // 세션 관리(동일 계정 동시 로그인 방지)
        http.sessionManagement(management -> management
                .sessionConcurrency(concurrency -> concurrency
                        .maximumSessions(1)                 // 계정당 세션 최대 1개
                        .maxSessionsPreventsLogin(false)    // 새 로그인 허용 + 기존 세션 만료 (true: 새 로그인 차단, false(기본값): 기존 세션 만료)
                        .sessionRegistry(sessionRegistry)   // 유저별 세션을 기록할 저장소
                        .expiredSessionStrategy(event ->           // 만료된 세션으로 요청이 오면 401 응답
                                event.getResponse().setStatus(HttpServletResponse.SC_UNAUTHORIZED))
                )
        );

        // 로그인 상태 유지 (세션이 없어도 remember-me 쿠키로 자동 로그인)
        http.rememberMe(remember -> remember
                .key(rememberMeKey)                      // 토큰 생성/검증에 쓰는 비밀 키
                .rememberMeParameter("remember-me")      // 로그인 요청에서 체크 여부를 받을 파라미터 이름
                .tokenValiditySeconds(86400 * 14)        // 유지 기간 14일 (초 단위)
                .userDetailsService(userDetailsService)  // 자동 로그인할 때 유저 정보를 조회할 서비스
        );

        return http.build();
    }


    // 유저별로 어떤 세션들이 있는지 기록하는 저장소 (권한 변경 시 해당 유저 세션을 찾아 만료시키는 용도)
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // HttpSession이 없어지면(로그아웃, 타임아웃) SessionRegistry에서도 해당 세션 정보를 지우기 위해 등록
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    // 비밀번호
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 권한 계층 구조 정의
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("""
                ROLE_ADMIN > ROLE_CHANNEL_MANAGER
                ROLE_CHANNEL_MANAGER > ROLE_USER
                """);
    }


    /**
     * 기본 핸들러는 위에서 정의한 RoleHierarchy를 모르기 때문에,
     * {@code @PreAuthorize}의 hasRole() 검사에도 권한 계층 구조가 적용되도록 핸들러를 교체
     *
     * @param roleHierarchy 위에서 정의한 권한 계층 구조
     * @return 권한 계층 구조가 적용된 핸들러
     */
    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(
            RoleHierarchy roleHierarchy
    ) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

}
