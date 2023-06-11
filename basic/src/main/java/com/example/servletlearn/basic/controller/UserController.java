package com.example.servletlearn.basic.controller;

import com.example.servletlearn.basic.bean.School;
import com.example.servletlearn.basic.bean.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/user")
public class UserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        School school = new School();
        school.setName("name");
        school.setAddress("here");

        User user = new User();
        user.setId(123);
        user.setName("Me");
        user.setSchool(school);

        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/One.jsp").forward(request, response);
    }
}
