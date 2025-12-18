package com.ibgs.studyAssistant.auth.dto;

import java.util.List;

public record AuthMeResponse(
        Integer id,
        String username,
        List<String> roles
) {}
