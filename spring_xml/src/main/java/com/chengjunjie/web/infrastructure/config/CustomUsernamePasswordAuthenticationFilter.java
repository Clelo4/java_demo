package com.chengjunjie.web.infrastructure.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;


public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    protected CustomUsernamePasswordAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // Extract username and password from the request
//        String username = obtainUsername(request);
//        String password = obtainPassword(request);

        // Create an Authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password");

        try {
            // Use the AuthenticationManager to authenticate the user
            Authentication authentication2 = this.getAuthenticationManager().authenticate(authentication);
            return authentication2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

