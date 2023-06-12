package com.example.servletlearn.mvc.controller;

import com.example.servletlearn.mvc.framework.GetMapping;
import com.example.servletlearn.mvc.framework.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class UserController {
    @GetMapping("/user")
    public ModelAndView index() {
        return new ModelAndView("./index.html", "user", "chengjunjie");
    }
}