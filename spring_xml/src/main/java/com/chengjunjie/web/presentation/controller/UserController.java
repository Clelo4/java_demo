package com.chengjunjie.web.presentation.controller;

import com.chengjunjie.web.application.UserService;
import com.chengjunjie.web.presentation.ControllerConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = ControllerConstant.user)
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/account", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String account() {
        return "{\"restSupport\":true}";
    }

    @GetMapping(value = "/user")
    public List<String> getAllUser() {
        return userService.getAllUser();
    }
}
