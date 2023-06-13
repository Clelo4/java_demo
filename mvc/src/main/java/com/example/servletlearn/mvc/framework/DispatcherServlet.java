package com.example.servletlearn.mvc.framework;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
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

@WebServlet(urlPatterns = "/*")
public class DispatcherServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<String, GetDispatcher> getMappings = new HashMap<>();
    private final Map<String, PostDispatcher> postMappings = new HashMap<>();
    private static final Set<Class<?>> supportedGetParameterTypes = Set.of(int.class, long.class, boolean.class,
            String.class, HttpServletRequest.class, HttpServletResponse.class, HttpSession.class);
    private static final Set<Class<?>> supportedPostParameterTypes = Set.of(HttpServletRequest.class,
            HttpServletResponse.class, HttpSession.class);
    private ViewEngine viewEngine;
    public DispatcherServlet() {
        super();
    }

    @Override
    public void init() {
        logger.info("init {}...", getClass().getSimpleName());

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
        this.viewEngine = new ViewEngine(getServletContext());
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
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        String path = request.getRequestURI().substring(request.getContextPath().length());
        AbstractDispatcher dispatcher = dispatcherMap.get(path);
        if (dispatcher == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ModelAndView mv;
        try {
            mv = dispatcher.invoke(request, response);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        if (mv == null) {
            return;
        }
        if (mv.getView().startsWith("redirect:")) {
            response.sendRedirect(mv.getView().substring(9));
            return;
        }
        PrintWriter pw = response.getWriter();
        this.viewEngine.render(mv, pw);
        pw.flush();
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
        if (method.getReturnType() != ModelAndView.class && method.getReturnType() != void.class) {
            throw new UnsupportedOperationException("Unsupported return type: " + method.getReturnType() + " for method: " + method);
        }
        for (Class<?> parameterClass : method.getParameterTypes()) {
            if (!supportedGetParameterTypes.contains(parameterClass)) {
                throw new UnsupportedOperationException("Unsupported parameter type: " + parameterClass + " for method: " + method);
            }
        }
        String path = method.getAnnotation(GetMapping.class).value();
        logger.info("Found GET: {} => {}", path, method);
        String[] parameterNames = Arrays.stream(method.getParameters()).map(Parameter::getName).toArray(String[]::new);
        this.getMappings.put(path, new GetDispatcher(controllerInstance, method, parameterNames, method.getParameterTypes()));
    }
    private void scanPostMappings(Object controllerInstance, Method method) {
    }
}
