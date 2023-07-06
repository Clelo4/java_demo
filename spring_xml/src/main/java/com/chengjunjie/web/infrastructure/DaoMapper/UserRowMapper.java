package com.chengjunjie.web.infrastructure.DaoMapper;

import com.chengjunjie.web.domain.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        Date date = rs.getDate("create_time");
        user.setCreateTime(date.getTime());

        return user;
    }
}
