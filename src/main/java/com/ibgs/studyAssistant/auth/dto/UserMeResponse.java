package com.ibgs.studyAssistant.auth.dto;

import java.util.List;
import java.util.UUID;

public record UserMeResponse(
        UUID id,
        String username,
        List<String> roles
) {}
