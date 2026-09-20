package com.sprint.mission.discodeit.common.aop.npush1;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Connection;

@Slf4j
@Aspect
@Component
public class NPlus1DetectorAop {
    private final ThreadLocal<QueryLoggingForm> currentLoggingForm = ThreadLocal.withInitial(QueryLoggingForm::new);


    @Around("execution( * javax.sql.DataSource.getConnection())")
    public Object captureConnetion(ProceedingJoinPoint joinPoint) throws Throwable {
        Connection connection = (Connection) joinPoint.proceed();
        return new ConnectionProxyHandler(connection, currentLoggingForm.get()).getProxy();
    }

    @After("within(@org.springframework.web.bind.annotation.RestController *)")
    public void loggingAfterApiFinish() {
        QueryLoggingForm loggingForm = currentLoggingForm.get();
        RequestAttributes attribute = RequestContextHolder.getRequestAttributes();

        if (attribute instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();

            loggingForm.setApiMethod(request.getMethod());
            loggingForm.setApiUrl(request.getRequestURI());
        }

        log.info("{}", loggingForm);
        currentLoggingForm.remove();
    }
}
