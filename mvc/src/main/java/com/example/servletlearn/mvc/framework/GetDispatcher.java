package com.example.servletlearn.mvc.framework;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GetDispatcher extends AbstractDispatcher {
    private final Object instance;
    private final Method method;
    private final String[] parameterNames;
    private final Class<?>[] parameterClasses;
    private final Class<?> returnClass;
    private final RenderEngine engine;

    public GetDispatcher(Object instance, Method method, String[] parameterNames, Class<?>[] parameterClasses, Class<?> returnClass, RenderEngine engine) {
        super();
        this.instance = instance;
        this.method = method;
        this.parameterNames = parameterNames;
        this.parameterClasses = parameterClasses;
        this.returnClass = returnClass;
        this.engine = engine;
    }

    @Override
    public void invoke(HttpServletRequest request, HttpServletResponse response) {
        Object[] arguments = new Object[parameterClasses.length];
        for (int i = 0; i < parameterClasses.length; i++) {
            String parameterName = parameterNames[i];
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
            } else if (parameterClass == int.class) {
                arguments[i] = Integer.valueOf(getOrDefault(request, parameterName, "0"));
            } else if (parameterClass == long.class) {
                arguments[i] = Long.valueOf(getOrDefault(request, parameterName, "0"));
            } else if (parameterClass == boolean.class) {
                arguments[i] = Boolean.valueOf(getOrDefault(request, parameterName, "false"));
            } else if (parameterClass == String.class) {
                arguments[i] = getOrDefault(request, parameterName, "");
            } else {
                throw new RuntimeException("Missing handle for type: " + parameterClass);
            }
        }

        try {
            Object data = this.method.invoke(this.instance, arguments);
            this.engine.render(request, response, this.returnClass, data);
        } catch (IllegalAccessException | InvocationTargetException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getOrDefault(HttpServletRequest request, String name, String defaultValue) {
        String s = request.getParameter(name);
        return s == null ? defaultValue : s;
    }
}
