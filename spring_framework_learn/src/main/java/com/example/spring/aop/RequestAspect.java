package com.example.spring.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestAspect {
    @Before("execution(public * com.example.spring.controller.*.*(..))")
    public void preRequest() {
        System.err.println("[Before] do request access check...");
    }
}
