package com.chengjunjie.web.infrastructure.config;

public interface StatusCodeProperties {
    int SUCCESS = 0;
    int NEED_LOGIN = 1;
    int ERROR_USERNAME_OR_PASSWORD = 2;
    int USER_NOT_FOUND = 3;
    int ERROR_PARAM = 4;
}
