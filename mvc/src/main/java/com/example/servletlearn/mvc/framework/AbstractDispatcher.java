package com.example.servletlearn.mvc.framework;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public abstract class AbstractDispatcher {
    public abstract void invoke(HttpServletRequest request, HttpServletResponse response);
}
