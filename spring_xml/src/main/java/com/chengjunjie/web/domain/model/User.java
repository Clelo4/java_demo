package com.chengjunjie.web.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(value = {"password"}, allowSetters = true)
public class User implements Serializable {

    int id;

    @NotBlank(message = "用户名不能为空")
    String username;

    @NotBlank(message = "密码不能为空")
    String password;

    @Email(message = "邮箱格式不正确")
    String email;

    Long createTime;
}
