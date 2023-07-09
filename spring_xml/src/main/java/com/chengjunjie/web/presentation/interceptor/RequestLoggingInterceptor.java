package com.chengjunjie.web.presentation.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;

public class RequestLoggingInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    private final ThreadLocal<StopWatch> stopWatchThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatchThreadLocal.set(stopWatch);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler,
                                Exception ex) {
        StopWatch stopWatch = stopWatchThreadLocal.get();
        stopWatch.stop();

        // 记录请求处理时间
        long executionTime = stopWatch.getTotalTimeMillis();
        logger.info("Request Method: {} - URL: {} - IP: {} - Time: {}ms", request.getMethod(), request.getRequestURL(), request.getRemoteAddr(), executionTime);
        if (ex != null) {
            logger.error("RequestLoggingInterceptor " + ex.getMessage(), ex);
        }
    }
}
