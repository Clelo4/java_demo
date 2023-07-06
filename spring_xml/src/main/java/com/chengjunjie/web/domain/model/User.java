package com.chengjunjie.web.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = {"password"}, allowSetters = true)
public class User {

    int id;

    String username;

    String password;

    String email;

    Long createTime;
}
