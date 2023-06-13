package com.example.servletlearn.mvc.controller;

import com.example.servletlearn.mvc.bean.User;
import com.example.servletlearn.mvc.framework.GetMapping;
import com.example.servletlearn.mvc.framework.ModelAndView;
import com.example.servletlearn.mvc.framework.PostMapping;

public class UserController {
    @GetMapping("/user")
    public ModelAndView index() {
        return new ModelAndView("./index.html", "user", "chengjunjie");
    }

    @PostMapping("/addUser")
    public ModelAndView addUser(User user) {
        return new ModelAndView("./index.html", "name", user.name);
    }
}
