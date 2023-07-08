package com.chengjunjie.web.infrastructure.DAO;

import com.chengjunjie.web.domain.model.User;

public interface UserDAO {
    User findById(int id);

    User findByUsername(String username);

    void create(User user);

    void update(User user);

    void delete(int id);
}
