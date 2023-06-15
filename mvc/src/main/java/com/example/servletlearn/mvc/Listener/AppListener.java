package com.example.servletlearn.mvc.Listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("WebApp initialized: ServletContext = " + sce.getServletContext());
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("WebApp destroyed: ServletContext = " + sce.getServletContext());
    }
}
