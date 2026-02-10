package com.ibgs.studyAssistant.studySession.dto;

import java.time.LocalDateTime;

public record StudySessionNameDTO (
        Integer id,
        String sessionName,
        LocalDateTime createdAt){}
