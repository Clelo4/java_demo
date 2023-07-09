package com.chengjunjie.web.infrastructure.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource(value = "classpath:global-config.properties")
public class GlobalConfigProperties {
    @Value("${USER_SESSION_NAME}")
    private String userSessionName;

    @Value("${CSRF_TOKEN}")
    private String csrfTokenName;

    @Value("${COOKIE_SECURE}")
    private boolean cookieSecure;
}
