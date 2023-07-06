package com.chengjunjie.web.infrastructure.DAO;


import com.chengjunjie.web.domain.model.User;
import com.chengjunjie.web.infrastructure.DaoMapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User findById(int id) {
        try {
            String sql = "SELECT * FROM user WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    @Override
    public void create(User user) {
        String sql = "INSERT INTO user (id, username, email, password, create_time) VALUES (?, ?, ?, ?, NOW())";
        jdbcTemplate.update(sql, user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE user SET username = ?, email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword(), user.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
