package com.example.servletlearn.basic.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/hi")
public class RedirectController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String redirectUrl = "/hi" + (name == null ? "" : "?name=" + name);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY); // 301
        response.setHeader("Location", "/hello");
    }
}
