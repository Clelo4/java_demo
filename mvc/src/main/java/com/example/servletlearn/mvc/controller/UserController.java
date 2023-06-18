package com.example.servletlearn.mvc.controller;

import com.example.servletlearn.mvc.bean.User;
import com.example.servletlearn.mvc.framework.GetMapping;
import com.example.servletlearn.mvc.framework.ModelAndView;
import com.example.servletlearn.mvc.framework.PostMapping;
import com.example.servletlearn.mvc.utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    @GetMapping("/user")
    public User index() {
        try {
            DB db = new DB();

            try (
                Connection conn = db.getConn();
                PreparedStatement stmt = conn.prepareStatement("SELECT username, email, password, create_time FROM user");
            ) {
                ResultSet rs = stmt.executeQuery();
                String username = null;
                String email = null;
                String password = null;
                String createTime = null;
                while (rs.next()) {
                    username = rs.getString(1);
                    email = rs.getString(2);
                    password = rs.getString(3);
                    createTime = rs.getString(4);
                }

                return new User(username, email, password, createTime);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/user/string")
    public String string() {
        try {
            DB db = new DB();
            db.query();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return "user";
    }

    @PostMapping("/addUser")
    public ModelAndView addUser(User user) {
        return new ModelAndView("./index.html", "name", user.username());
    }
}
