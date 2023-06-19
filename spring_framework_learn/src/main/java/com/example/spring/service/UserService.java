package com.example.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Component
public class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<User> getUsers(int pageIndex, int pageSize) {
        int offset = pageSize * (pageIndex - 1);
        return jdbcTemplate.query(
                "SELECT * FROM user LIMIT ? OFFSET ?",
                new DataClassRowMapper<>(User.class),
                pageSize,
                offset);
    }

    public User getUserById(long id) {
        return jdbcTemplate.execute((Connection connection) -> {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM user WHERE id = ?")) {
                ps.setObject(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new User(
                                rs.getLong("id"),
                                rs.getString("username"),
                                rs.getString("email")
                        );
                    }
                    throw new RuntimeException("user not found by id.");
                }
            }
        });
    }

    public User getUserByName(String username) {
        return jdbcTemplate.execute("SELECT * FROM user WHERE username = ?", (PreparedStatement ps) -> {
            ps.setObject(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getLong("id"), rs.getString("username"), rs.getString("email"));
                }
                throw new RuntimeException("user not found by username");
            }
        });
    }

    public User getUserByEmail(String email) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM user WHERE email = ?",
                (ResultSet rs, int rowNum) -> {
                    return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email")
                    );
                },
                email);
    }

    public User addUser(String username, String email, String password) {

        KeyHolder holder = new GeneratedKeyHolder();
        if (1 != jdbcTemplate.update(
                (conn) -> {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO user (username, email, password) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, username);
                    ps.setObject(2, email);
                    ps.setObject(3, password);
                    return ps;
                },
                holder)
        ) {
            throw new RuntimeException("add user field");
        }

        return new User(Objects.requireNonNull(holder.getKey()).longValue(), username, email);
    }
}
