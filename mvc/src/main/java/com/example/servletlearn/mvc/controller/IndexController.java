package com.example.servletlearn.mvc.controller;

import com.example.servletlearn.mvc.framework.GetMapping;
import com.example.servletlearn.mvc.framework.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class IndexController {
    @GetMapping("/hi")
    public ModelAndView index() {
        return new ModelAndView("./index.html", "user", "chengjunjie");
    }

    @GetMapping("/hello")
    public ModelAndView echo(HttpServletRequest request, String name, String content) {
        Map<String, Object> model = new HashMap<>();
        model.put("name", name);
        model.put("content", content);
        return new ModelAndView("./index.html", model);
    }
}