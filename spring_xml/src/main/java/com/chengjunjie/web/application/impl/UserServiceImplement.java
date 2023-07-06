package com.chengjunjie.web.application.impl;

import com.chengjunjie.web.application.UserService;
import com.chengjunjie.web.domain.model.Result;
import com.chengjunjie.web.domain.model.User;
import com.chengjunjie.web.infrastructure.DAO.UserDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.chengjunjie.web.presentation.ControllerConstant.user;

@Component
public class UserServiceImplement implements UserService {
    @Autowired
    UserDAO userDAO;

    public Result<Object> isLogin(HttpSession session) {
        return null;
    }

    @Override
    public Result<User> login(User user) {
        return null;
    }

    @Override
    public Result<User> getUserById(int id) {
        Result<User> result = new Result<>();
        // 先去数据库找用户名是否存在
        User getUser = userDAO.findById(id);
        if (getUser == null) {
            result.setResultFailed("user not found");
            return result;
        }
        // 返回成功消息
        result.setResultSuccess("成功！", getUser);
        return result;
    }
}

