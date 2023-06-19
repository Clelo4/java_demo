package com.example.spring.controller;

import com.example.spring.service.User;
import com.example.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    UserService userService;
    @GetMapping("/users")
    public List<User> users() {
        return UserService.getUsers();
    }
}
