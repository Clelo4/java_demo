package com.chengjunjie.web;

import jakarta.servlet.*;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.EnumSet;

public class AppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        XmlWebApplicationContext dispatcherContext = new XmlWebApplicationContext();
        dispatcherContext.setConfigLocations("/WEB-INF/web-context.xml", "/WEB-INF/app-context.xml");

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");
        dispatcher.setAsyncSupported(true);

        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding("UTF-8");
        FilterRegistration.Dynamic characterEncodingFilterRegistration = servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
        characterEncodingFilterRegistration.setAsyncSupported(true);
        characterEncodingFilterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC), true, "/*");


        String sessionFilterName = "springSessionRepositoryFilter";
        DelegatingFilterProxy springSessionRepositoryFilter = new DelegatingFilterProxy(sessionFilterName);
        FilterRegistration.Dynamic springSessionRepositoryFilterRegistration = servletContext.addFilter(sessionFilterName, springSessionRepositoryFilter);
        if (springSessionRepositoryFilterRegistration == null) {
            throw new IllegalStateException("Duplicate Filter registration for '" + springSessionRepositoryFilter + "'. Check to ensure the Filter is only configured once.");
        } else {
            springSessionRepositoryFilterRegistration.setAsyncSupported(true);
            EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.ASYNC);
            springSessionRepositoryFilterRegistration.addMappingForUrlPatterns(dispatcherTypes, false, new String[]{"/*"});
        }

        /*
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
        FilterRegistration.Dynamic delegatingFilterProxyRegistration = servletContext.addFilter("springSecurityFilterChain", delegatingFilterProxy);
        delegatingFilterProxyRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC), true, "/*");
*/
    }
}
