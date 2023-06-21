package com.example.spring.controller;

import com.example.spring.aop.MetricTime;
import com.example.spring.service.User;
import com.example.spring.service.UserService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    UserService userService;

    record UserRequestBody(String username, String email, String password) {}

    @MetricTime("getUsers")
    @GetMapping("/users")
    public List<User> users() {
        return userService.getUsers(1, 10);
    }

    @GetMapping("/getUserById")
    public User getUserById(@RequestParam(name = "id") long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getUserByName")
    public User getUserByName(@RequestParam(name = "name") String name) {
        return userService.getUserByName(name);
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody UserRequestBody user) {
        return userService.addUser(user.username(), user.email(), user.password());
    }
}
