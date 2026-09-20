package com.sprint.mission.discodeit.common.aop.npush1;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;


@RequiredArgsConstructor
public class ConnectionProxyHandler implements MethodInterceptor {
    private static final String JDBC_PREPARE_STATEMENT_METHOD_NAME = "prepareStatement";

    private final Object connection;
    private final QueryLoggingForm loggingForm;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();

        if (result != null && isPrepareStatement(invocation)) {
            ProxyFactory proxyFactory = new ProxyFactory(result);
            proxyFactory.addAdvice((new PreparedStatementProxyhandler(loggingForm)));
            return proxyFactory.getProxy();
        }

        return result;
    }

    private boolean isPrepareStatement(MethodInvocation invocation) {
        return JDBC_PREPARE_STATEMENT_METHOD_NAME.equals(invocation.getMethod().getName());
    }

    public Object getProxy() {
        ProxyFactory proxyFactory = new ProxyFactory(connection);
        proxyFactory.addAdvice(this);
        return proxyFactory.getProxy();

    }
}
