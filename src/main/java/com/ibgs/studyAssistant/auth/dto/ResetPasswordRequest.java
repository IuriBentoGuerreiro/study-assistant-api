package com.ibgs.studyAssistant.auth.dto;

public record ResetPasswordRequest(
        String token,
        String newPassword
) {}
