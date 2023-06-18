package com.example.servletlearn.mvc.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class PostDispatcher extends AbstractDispatcher {
    private final Object instance;
    private final Method method;
    private final Class<?>[] parameterClasses;
    private final Class<?> returnClass;
    private final RenderEngine engine;

    public PostDispatcher(Object instance, Method method, Class<?>[] parameterClasses, Class<?> returnClass, RenderEngine engine) {
        this.instance = instance;
        this.method = method;
        this.parameterClasses = parameterClasses;
        this.returnClass = returnClass;
        this.engine = engine;
    }

    @Override
    public void invoke(HttpServletRequest request, HttpServletResponse response) {
        Object[] arguments = new Object[parameterClasses.length];
        for (int i = 0; i < parameterClasses.length; ++i) {
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
            } else {
                BufferedReader buffer = null;
                try {
                    buffer = request.getReader();
//                    arguments[i] = this.objectMapper.readValue(buffer, parameterClass);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
//        return (ModelAndView) this.method.invoke(instance, arguments);
    }
}
