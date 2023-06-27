package com.chengjunjie.web.presentation.controller;

import com.chengjunjie.web.presentation.ControllerConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = ControllerConstant.user)
public class UserController {
    @GetMapping(value = "/account", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String account() {
        return "{\"restSupport\":true}";
    }
}
