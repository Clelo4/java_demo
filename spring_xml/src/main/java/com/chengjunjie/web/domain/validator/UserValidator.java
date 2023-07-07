package com.chengjunjie.web.domain.validator;

import com.chengjunjie.web.domain.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "field.required", "用户名不能为空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required", "密码不能为空");

        User user = (User) target;
        if (user.getUsername() == null || user.getUsername().length() < 6) {
            errors.rejectValue("username", "field.minlength", "用户名长度不能小于6");
        }
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            errors.rejectValue("password", "field.minlength", "密码长度不能小于8");
        }
    }
}
