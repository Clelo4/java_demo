package com.example.servletlearn.mvc.controller;

import com.example.servletlearn.mvc.bean.User;
import com.example.servletlearn.mvc.framework.GetMapping;
import com.example.servletlearn.mvc.framework.ModelAndView;
import com.example.servletlearn.mvc.framework.PostMapping;
import com.example.servletlearn.mvc.utils.DB;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;

public class UserController {
    @GetMapping("/user")
    public User index() {
        try {
            DB db = new DB();
            db.query();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        User user = new User();
        user.username = "jack";
        user.password = "password";
        user.email = "email";

        return user;
    }
    @GetMapping("/user/string")
    public String string() {
        try {
            DB db = new DB();
            db.query();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        User user = new User();
        user.username = "jack";
        user.password = "password";
        user.email = "email";

        return "user";
    }

    @PostMapping("/addUser")
    public ModelAndView addUser(User user) {
        return new ModelAndView("./index.html", "name", user.username);
    }
}
