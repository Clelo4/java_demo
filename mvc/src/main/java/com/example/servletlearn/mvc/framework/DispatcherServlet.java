package com.example.servletlearn.mvc.framework;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class DispatcherServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<String, GetDispatcher> getMappings = new HashMap<>();
    private final Map<String, PostDispatcher> postMappings = new HashMap<>();
    private static final Set<Class<?>> supportedGetParameterTypes = Set.of(int.class, long.class, boolean.class,
            String.class, HttpServletRequest.class, HttpServletResponse.class, HttpSession.class);
    private static final Set<Class<?>> supportedNativePostParameterTypes = Set.of(HttpServletRequest.class,
            HttpServletResponse.class, HttpSession.class);

    private static final Set<Class<?>> supportedReturnTYpe = Set.of(ModelAndView.class, String.class);

    private RenderEngine renderEngine;
    public DispatcherServlet() {
        super();
    }

    @Override
    public void init() {
        logger.info("init {}...", getClass().getSimpleName());
        this.renderEngine = new RenderEngine(getServletContext());

        List<Class<?>> controllers = this.scanControllers("com.example.servletlearn.mvc.controller");

        for (Class<?> controllerClass : controllers) {
            try {
                Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

                for (Method method : controllerClass.getDeclaredMethods()) {
                    if (method.getAnnotation(GetMapping.class) != null) {
                        scanGetMappings(controllerInstance, method);
                    } else if (method.getAnnotation(PostMapping.class) != null) {
                        scanPostMappings(controllerInstance, method);
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        process(request, response, this.getMappings);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        process(request, response, this.postMappings);
    }

    private void process(HttpServletRequest request, HttpServletResponse response, Map<String, ? extends AbstractDispatcher> dispatcherMap) throws IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        AbstractDispatcher dispatcher = dispatcherMap.get(path);

        if (dispatcher == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        dispatcher.invoke(request, response);
    }

    private List<Class<?>> scanControllers(String packageName) {
        return scanControllersV1(packageName);
    }

    private List<Class<?>> scanControllersV1(String packageName) {
        ServletContext context = getServletContext();
        Set<String> fileSet = context.getResourcePaths("/WEB-INF/classes/" + packageName.replaceAll("[.]", "/"));
        List<Class<?>> controllers = new ArrayList<>();
        fileSet.stream()
            .filter(classFullName -> classFullName.endsWith(".class"))
            .forEach(classFullName -> {
                try {
                    String className = classFullName.substring(classFullName.lastIndexOf("/") + 1, classFullName.lastIndexOf("."));
                    Class<?> res = Class.forName(packageName + "." + className);
                    controllers.add(res);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        return controllers;
    }

    private void scanGetMappings(Object controllerInstance, Method method) {
        for (Class<?> parameterClass : method.getParameterTypes()) {
            if (!supportedGetParameterTypes.contains(parameterClass)) {
                throw new UnsupportedOperationException("Unsupported parameter type: " + parameterClass + " for method: " + method);
            }
        }
        String path = method.getAnnotation(GetMapping.class).value();
        logger.info("Found GET: {} => {}", path, method);
        String[] parameterNames = Arrays.stream(method.getParameters()).map(Parameter::getName).toArray(String[]::new);
        this.getMappings.put(path, new GetDispatcher(controllerInstance, method, parameterNames, method.getParameterTypes(), method.getReturnType(), this.renderEngine));
    }
    private void scanPostMappings(Object controllerInstance, Method method) {
        int customBeanCounter = 0;
        for (Class<?> parameterClass : method.getParameterTypes()) {
            if (!supportedNativePostParameterTypes.contains(parameterClass)) {
                customBeanCounter++;
            }
        }
        if (customBeanCounter > 1) throw new UnsupportedOperationException("Unsupported more than one parameter type for method: " + method);
        String path = method.getAnnotation(PostMapping.class).value();
        logger.info("Found POST: {} => {}", path, method);
        this.postMappings.put(path, new PostDispatcher(controllerInstance, method, method.getParameterTypes(), method.getReturnType(), this.renderEngine));
    }
}
