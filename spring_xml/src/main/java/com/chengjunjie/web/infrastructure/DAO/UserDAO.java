package com.chengjunjie.web.infrastructure.DAO;

import com.chengjunjie.web.domain.model.User;

import java.util.List;

public interface UserDAO {
    User findById(int id);

    void create(User user);

    void update(User user);

    void delete(int id);
}
