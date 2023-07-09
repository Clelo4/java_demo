package com.chengjunjie.web.presentation.interceptor;

import com.chengjunjie.web.application.UserService;
import com.chengjunjie.web.domain.model.Result;
import com.chengjunjie.web.infrastructure.config.GlobalConfigProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    private UserService userService;
    private final ObjectMapper objectMapper;
    private GlobalConfigProperties globalConfigProperties;

    public SecurityInterceptor() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setGlobalConfigProperties(GlobalConfigProperties globalConfigProperties) {
        this.globalConfigProperties = globalConfigProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        boolean res = true;
        HttpSession session = request.getSession(false);

        Result<?> result = new Result<>();

        // 验证身份
        if (session == null || !userService.isLogin(session).isSuccess()) {
            logger.info(request.getRequestURI() + " - need login");

            result.setResultFailed(1, "请登录");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(result));
            writer.flush();
            writer.close();
            return false;
        }

        String csrfToken = request.getHeader(globalConfigProperties.getCsrfTokenName());
        // CSRF检查
        if (csrfToken == null || !csrfToken.equals((String) session.getAttribute(globalConfigProperties.getCsrfTokenName()))) {
            logger.info(request.getRequestURI() + " - 非法请求");

            result.setResultFailed(1, "非法请求");

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(result));
            writer.flush();
            writer.close();
            return false;
        }

        return res;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler,
                                @Nullable Exception ex) {
    }
}
