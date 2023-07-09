package com.chengjunjie.web.presentation.controller;

import com.chengjunjie.web.presentation.ControllerConstant;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = ControllerConstant.httpCode)
public class HttpCodeController {
    @GetMapping(value = "/{code}")
    public ResponseEntity<String> getCode(@PathVariable int code) {
        throw new RuntimeException("123");
//        return new ResponseEntity<>(String.valueOf(code), HttpStatusCode.valueOf(code));
    }
}
