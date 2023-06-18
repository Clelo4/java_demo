package com.example.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @GetMapping("/hello")
    public ModelAndView hello() {
        return new ModelAndView("hello.html"); // 仅View，没有Model
    }
}
