package com.ibgs.studyAssistant.auth.service;

public interface EmailService {
    void sendPasswordResetEmail(String to, String token);
}
