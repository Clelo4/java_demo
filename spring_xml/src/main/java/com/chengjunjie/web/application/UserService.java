package com.chengjunjie.web.application;

import com.chengjunjie.web.domain.model.Result;
import com.chengjunjie.web.domain.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public Result<Object> isLogin(HttpSession session);

    public Result<User> login(User user);

    public Result<User> getUserById(int id);
}
