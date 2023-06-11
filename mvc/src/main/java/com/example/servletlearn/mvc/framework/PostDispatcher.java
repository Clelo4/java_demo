package com.example.servletlearn.mvc.framework;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public class PostDispatcher extends AbstractDispatcher {
    final Object instance;
    final Method method;
    final Class<?>[] parameterClasses;

    public PostDispatcher(Object instance, Method method, Class<?>[] parameterClasses) {
        this.instance = instance;
        this.method = method;
        this.parameterClasses = parameterClasses;
//        this.objectMapper = objectMapper;
    }

    @Override
    public ModelAndView invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ReflectiveOperationException {
        return null;
    }
}
