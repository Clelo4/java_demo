package com.example.servletlearn.mvc.framework;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.Servlet5Loader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.io.Writer;

/**
 * View engine
 */
public class ViewEngine {
    private final PebbleEngine engine;

    public ViewEngine(ServletContext servletContext) {
        Servlet5Loader loader = new Servlet5Loader(servletContext);
        loader.setCharset("UTF-8");
        loader.setPrefix("/WEB-INF/templates");
        loader.setSuffix("");
        this.engine = new PebbleEngine.Builder().autoEscaping(true)
                .cacheActive(false).loader(loader).build();
    }

    public void render(ModelAndView mv, Writer writer) throws IOException {
        PebbleTemplate compiledTemplate = engine.getTemplate(mv.getView());
        compiledTemplate.evaluate(writer, mv.getModel());
    }
}
