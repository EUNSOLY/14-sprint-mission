package com.sprint.mission.discodeit.common.security.handler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

@Component
public final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
    // 토큰을 가공 없이 원본 그대로 비교하는 핸들러 (SPA가 쿠키에서 읽어 헤더로 보낸 토큰 검증용)
    private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
    // 토큰을 요청마다 무작위로 인코딩해서 내려주고 검증 시 디코딩하는 핸들러 (BREACH 공격 방어, 폼 파라미터 검증용)
    private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        // 응답 본문에 토큰이 노출될 때 BREACH 공격을 막기 위해 항상 xor 핸들러를 사용
        this.xor.handle(request, response, csrfToken);
        // 지연 로딩되는 토큰을 강제로 읽어서 XSRF-TOKEN 쿠키로 내려보냄
        csrfToken.get();
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        String headerValue = request.getHeader(csrfToken.getHeaderName());
        // 헤더에 토큰이 있으면 plain 핸들러로 해석 (SPA가 쿠키의 원본 토큰을 헤더에 담아 보내는 경우)
        // 헤더가 없으면 xor 핸들러로 해석 (서버 렌더링 폼이 _csrf 파라미터로 보내는 경우)
        return (StringUtils.hasText(headerValue) ? this.plain : this.xor).resolveCsrfTokenValue(request, csrfToken);

    }
}
