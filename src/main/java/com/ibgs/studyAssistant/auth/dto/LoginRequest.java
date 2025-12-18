package com.ibgs.studyAssistant.auth.dto;

public record LoginRequest(
        String username,
        String password
) {}
