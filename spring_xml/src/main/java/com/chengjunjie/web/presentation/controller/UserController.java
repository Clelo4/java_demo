package com.chengjunjie.web.presentation.controller;

import com.chengjunjie.web.application.UserService;
import com.chengjunjie.web.domain.model.Result;
import com.chengjunjie.web.domain.model.User;
import com.chengjunjie.web.presentation.ControllerConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ControllerConstant.user)
public class UserController {
    public static final String SESSION_NAME = "userInfo";

    @Autowired
    private UserService userService;

    @GetMapping(value = "/account", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String account() {
        return "{\"restSupport\":true}";
    }

    @GetMapping(value = "/getUserById")
    public Result<User> getUserById(@RequestParam int id) {
        return userService.getUserById(id);
    }

    @PostMapping("/login")
    public Result<User> login(@Validated @RequestBody User user, BindingResult errors, HttpServletRequest request) {
        Result<User> result;
        // 如果校验有错，返回登录失败以及错误信息
        if (errors.hasErrors()) {
            result = new Result<>();
            result.setResultFailed(errors.getFieldError().getDefaultMessage());
            return result;
        }
        // 调用登录服务
        result = userService.login(user);
        // 如果登录成功，则设定session
        if (result.isSuccess()) {
            request.getSession().setAttribute(SESSION_NAME, result.getData());
        }
        return result;
    }
}
