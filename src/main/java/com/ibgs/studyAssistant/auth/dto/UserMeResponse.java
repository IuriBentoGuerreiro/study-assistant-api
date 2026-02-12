package com.ibgs.studyAssistant.auth.dto;

import java.util.List;

public record UserMeResponse(
        Integer id,
        String username,
        List<String> roles
) {}
