package com.example.servletlearn.mvc.framework;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.Servlet5Loader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
/**
 * View engine
 */
public class RenderEngine {
    private final PebbleEngine templateEngine;
    private final ObjectMapper objectMapper;

    public RenderEngine(ServletContext servletContext) {
        Servlet5Loader loader = new Servlet5Loader(servletContext);
        loader.setCharset("UTF-8");
        loader.setPrefix("/WEB-INF/templates");
        loader.setSuffix("");
        this.templateEngine = new PebbleEngine.Builder().autoEscaping(true)
                .cacheActive(false).loader(loader).build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void render(HttpServletRequest request, HttpServletResponse response, Class<?> returnClass, Object returnObj) throws IOException {
        PrintWriter writer = response.getWriter();
        response.setCharacterEncoding("UTF-8");

        if (returnClass == ModelAndView.class) {
            response.setContentType("text/html");

            ModelAndView mv = (ModelAndView) returnObj;
            PebbleTemplate compiledTemplate = templateEngine.getTemplate(mv.getView());
            compiledTemplate.evaluate(writer, mv.getModel());
        } else {
            response.setContentType("application/json");

            writer.write(objectMapper.writeValueAsString(returnObj));
        }

        writer.flush();
        response.flushBuffer();
    }
}
