package com.chengjunjie.web.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getAllUser() {
        String sql = "SELECT username FROM user";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
