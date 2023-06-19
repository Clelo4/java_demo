package com.example.spring.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.example.spring.service.User;

@Controller
public class UserController {
    @GetMapping("/hello")
    public ModelAndView hello() {
        return new ModelAndView("hello.html"); // 仅View，没有Model
    }
    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }
    @PostMapping("/signin")
    public ModelAndView doSignin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session) {
        return new ModelAndView("hello.html"); // 仅View，没有Model
    }
    @PostMapping(value = "/rest",
            consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String rest(@RequestBody User user) {
        return "{\"restSupport\":true}";
    }
}
