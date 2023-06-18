package com.example.servletlearn.mvc.controller;

import com.example.servletlearn.mvc.bean.Response;
import com.example.servletlearn.mvc.bean.User;
import com.example.servletlearn.mvc.framework.GetMapping;
import com.example.servletlearn.mvc.framework.ModelAndView;
import com.example.servletlearn.mvc.framework.PostMapping;
import com.example.servletlearn.mvc.utils.DB;

import java.sql.*;

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
    public Response addUser(User user) {
        try {
            DB db = new DB();

            try (
                    Connection conn = db.getConn();
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO user (username, email, password) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ) {
                stmt.setString(1, user.username());
                stmt.setString(2, user.email());
                stmt.setString(3, user.password());
                int n = stmt.executeUpdate();

                if (n == 0) return new Response(1, "ERROR");

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long id = rs.getLong(1); // 注意：索引从1开始
                        return new Response(0, "success: " + id);
                    }
                }

                return new Response(0, "success");
            }

        } catch (SQLException e) {
            return new Response(e.getErrorCode(), e.getMessage());
        }
    }
}
