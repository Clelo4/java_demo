package com.chengjunjie.web.presentation.controller;

import com.chengjunjie.web.domain.model.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<?>> handleException(Exception ex, HttpServletRequest request) {
        logger.info("GlobalExceptionHandler Method: {} - URL: {} - IP: {} - Error: {}", request.getMethod(), request.getRequestURL(), request.getRemoteAddr(), ex.getMessage());

        Result<?> result = new Result<>();
        result.setResultFailed(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
