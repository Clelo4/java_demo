package com.chengjunjie.web.application.impl;

import com.chengjunjie.web.application.UserService;
import com.chengjunjie.web.domain.model.Result;
import com.chengjunjie.web.domain.model.User;
import com.chengjunjie.web.infrastructure.DAO.UserDAO;
import com.chengjunjie.web.infrastructure.config.GlobalConfigProperties;
import com.chengjunjie.web.infrastructure.config.StatusCodeProperties;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImplement implements UserService {

    private UserDAO userDAO;
    private GlobalConfigProperties globalConfigProperties;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setGlobalConfigProperties(GlobalConfigProperties globalConfigProperties) {
        this.globalConfigProperties = globalConfigProperties;
    }

    public Result<User> isLogin(@Nullable HttpSession session) {
        Object userObj;
        Result<User> res = new Result<>();
        if (session == null || ((userObj = session.getAttribute(globalConfigProperties.getUserSessionName())) == null)) {
            res.setResultFailed(StatusCodeProperties.NEED_LOGIN, "非法访问");
        } else {
            res.setResultSuccess("验证成功", (User) userObj);
        }
        return res;
    }

    @Override
    public Result<User> login(@NotNull User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        User findUser = userDAO.findByUsername(username);

        Result<User> res = new Result<>();
        if (findUser == null || !findUser.getPassword().equals(password)) {
            res.setResultFailed(StatusCodeProperties.ERROR_USERNAME_OR_PASSWORD, "用户或密码错误");
        } else {
            res.setResultSuccess("成功", findUser);
        }
        return res;
    }

    @Override
    public Result<User> getUserById(int id) {
        Result<User> result = new Result<>();
        // 先去数据库找用户名是否存在
        User getUser = userDAO.findById(id);
        if (getUser == null) {
            result.setResultFailed(StatusCodeProperties.USER_NOT_FOUND, "user not found");
            return result;
        }
        // 返回成功消息
        result.setResultSuccess("成功！", getUser);
        return result;
    }
}

