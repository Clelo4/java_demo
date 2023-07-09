package com.chengjunjie.web.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

@Component
public class SessionCleanup {
    private SessionRepository<?> sessionRepository;

    @Autowired
    public void setSessionRepository(SessionRepository<?> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void deleteSessionById(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}
