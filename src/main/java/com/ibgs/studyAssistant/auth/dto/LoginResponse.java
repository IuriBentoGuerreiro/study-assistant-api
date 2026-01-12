package com.ibgs.studyAssistant.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {}
