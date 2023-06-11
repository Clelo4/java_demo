package com.example.servletlearn.mvc.framework;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private final Map<String, Object> model;
    private final String view;

    public ModelAndView(String view) {
        this.view = view;
        this.model = Map.of();
    }
    public ModelAndView(String view, String name, Object value) {
        this.view = view;
        this.model = new HashMap<>();
        this.model.put(name, value);
    }
    public ModelAndView(String view, Map<String, Object> model) {
        this.view = view;
        this.model = new HashMap<>(model);
    }

    public String getView() {
        return this.view;
    }
    public Map<String, Object> getModel() {
        return this.model;
    }
}
