package com.chengjunjie.web.presentation.controller;

import com.chengjunjie.web.application.UserService;
import com.chengjunjie.web.domain.model.Result;
import com.chengjunjie.web.domain.model.User;
import com.chengjunjie.web.infrastructure.config.GlobalConfigProperties;
import com.chengjunjie.web.infrastructure.config.StatusCodeProperties;
import com.chengjunjie.web.presentation.ControllerConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value = ControllerConstant.user)
public class UserController {

    private UserService userService;
    private GlobalConfigProperties globalConfigProperties;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setGlobalConfigProperties(GlobalConfigProperties globalConfigProperties) {
        this.globalConfigProperties = globalConfigProperties;
    }

    @GetMapping(value = "/userInfo")
    public Result<User> userInfo(HttpServletRequest request) {
        Result<User> sessionUserInfo = userService.isLogin(request.getSession(false));
        if (sessionUserInfo.isSuccess()) {
            User findSessionUser = sessionUserInfo.getData();
            return userService.getUserById(findSessionUser.getId());
        } else {
            return sessionUserInfo;
        }
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody(required = false) @Validated User user, Errors errors, HttpServletRequest request, HttpServletResponse response) {
        Result<User> result;
        if (user == null) {
            result = new Result<>();
            result.setResultFailed(StatusCodeProperties.ERROR_PARAM,"请求体不能为空");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return result;
        }

        // 如果校验有错，返回登录失败以及错误信息
        if (errors.hasErrors()) {
            result = new Result<>();
            FieldError fieldError = Objects.requireNonNull(errors.getFieldError());
            result.setResultFailed(-1, fieldError.getDefaultMessage());
            return result;
        }

        // 调用登录服务
        result = userService.login(user);
        // 如果登录成功，则设定session
        if (result != null && result.isSuccess()) {
            String csrfToken = UUID.randomUUID().toString();
            Cookie cookie = this.createEmptyCsrfTokenCookie();
            cookie.setValue(csrfToken);
            response.addCookie(cookie);

            HttpSession session = request.getSession();

            session.setAttribute(globalConfigProperties.getUserSessionName(), result.getData());
            session.setAttribute(globalConfigProperties.getCsrfTokenName(), csrfToken);
        }

        return result;
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Result<String> result = new Result<>();

        HttpSession session = request.getSession(false);
        if (session == null) {
            result.setResultFailed(-1, "非登录用户");
            return result;
        }

        Result<User> sessionUserInfo = userService.isLogin(session);
        if (sessionUserInfo.isSuccess()) {
            session.invalidate();

            Cookie cookie = this.createEmptyCsrfTokenCookie();
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            result.setResultSuccess("退出登录成功");
        } else {
            result.setResultFailed(-1, "非登录用户");
        }

        return result;
    }

    private Cookie createEmptyCsrfTokenCookie() {
        Cookie cookie = new Cookie(globalConfigProperties.getCsrfTokenName(), null);
        cookie.setHttpOnly(false);
        cookie.setSecure(globalConfigProperties.isCookieSecure());
        cookie.setAttribute("SameSite", "true");
        cookie.setPath("/");

        return cookie;
    }
}
