package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * HandlerInterceptor : 컨트롤러로 들어오는 요청에 대한 전/후 처리를 가능하게 해주는 인터페이스
 * preHandle: 컨트롤러 호출 전에 호출되는 메서드 (false로 반환되면 다음 차례 컨트롤러 미실행)
 * postHandle: 컨트롤러가 정상적으로 실행된 이후에 실행 (컨트롤러에서 예외 발생 시 미실행)
 * afterCompletion: 컨트롤러가 응답을 전송한 뒤에 실행되는 메서드 (컨트롤러에서 예외발생 시 4번째 파라미터로 전달되어 로그를 남기는 후처리 가능)
 */
@Slf4j
@Component
public class MDCLoggingInterceptor implements HandlerInterceptor {
    public static final String REQUEST_ID_HEADER = "Discodeit-Request-ID";
    public static final String REQUEST_ID = "requestId";
    public static final String REQUEST_URL = "requestUrl";
    public static final String REQUEST_METHOD = "requestMethod";

    @Override // 컨트롤러의 메서드에 매핑된 특정 URI가 호출됐을 때 실행되는 메서드로, 컨트롤러를 경유(접근)하기 직전에 실행되는 메서드
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws Exception {
        String requestId = UUID.randomUUID().toString();

        MDC.put(REQUEST_ID, requestId);
        MDC.put(REQUEST_METHOD, request.getMethod());
        MDC.put(REQUEST_URL, request.getRequestURI());

        response.setHeader(REQUEST_ID_HEADER, requestId);

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        // 스레드 재사용 시 이전 요청의 값이 남지 않도록 정리
        MDC.clear();
    }
}
