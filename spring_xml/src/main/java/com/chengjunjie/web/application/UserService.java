package com.chengjunjie.web.application;

import com.chengjunjie.web.domain.model.Result;
import com.chengjunjie.web.domain.model.User;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Result<User> isLogin(@Nullable HttpSession session);

    Result<User> login(@NotNull User user);

    Result<User> getUserById(int id);
}
