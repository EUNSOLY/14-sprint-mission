package com.sprint.mission.discodeit.common.aop.npush1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class PreparedStatementProxyhandler implements MethodInterceptor {

    private static final Set<String> JDBC_QUERY_METHODS = Set.of("executeQuery", "execute", "executeUpdate");

    private final QueryLoggingForm loggingForm;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (!JDBC_QUERY_METHODS.contains(invocation.getMethod().getName())) {
            return invocation.proceed();
        }

        long startTime = System.currentTimeMillis();
        Object result = invocation.proceed();
        long endTime = System.currentTimeMillis();

        loggingForm.addQueryTime(endTime - startTime);
        loggingForm.queryCountUp();
        return result;
    }
}
