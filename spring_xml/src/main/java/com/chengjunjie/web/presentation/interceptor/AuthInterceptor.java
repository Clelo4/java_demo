package com.chengjunjie.web.presentation.interceptor;

import com.chengjunjie.web.application.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler)
            throws Exception {
        HttpSession session = request.getSession();

        if (!userService.isLogin(session).isSuccess()) {
            PrintWriter writer = response.getWriter();
            writer.write("{ \"code\": 1, \"message\": \"need login\" }");
            writer.flush();
            writer.close();
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler,
                                @Nullable Exception ex) {
        System.out.println("afterCompletion: " + request.getRequestURI());
    }
}
