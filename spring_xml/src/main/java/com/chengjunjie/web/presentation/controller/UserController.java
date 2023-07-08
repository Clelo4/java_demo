package com.chengjunjie.web.presentation.controller;

import com.chengjunjie.web.application.UserService;
import com.chengjunjie.web.domain.model.Result;
import com.chengjunjie.web.domain.model.User;
import com.chengjunjie.web.infrastructure.config.GlobalConfigProperties;
import com.chengjunjie.web.infrastructure.config.StatusCodeProperties;
import com.chengjunjie.web.presentation.ControllerConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
        Result<User> sessionUserInfo = userService.isLogin(request.getSession());
        if (sessionUserInfo.isSuccess()) {
            User findSessionUser = sessionUserInfo.getData();
            return userService.getUserById(findSessionUser.getId());
        } else {
            return sessionUserInfo;
        }
    }

    @PostMapping("/login")
    @Validated
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
            result.setResultFailed(
                    Integer.parseInt(fieldError.getCode() == null ? "-1" : fieldError.getCode()),
                    fieldError.getDefaultMessage()
            );
            return result;
        }

        // 调用登录服务
        result = userService.login(user);
        // 如果登录成功，则设定session
        if (result != null && result.isSuccess()) {
            request.getSession().setAttribute(globalConfigProperties.getUserSessionName(), result.getData());
        }

        return result;
    }
}
