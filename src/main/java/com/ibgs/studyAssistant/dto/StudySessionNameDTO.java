package com.ibgs.studyAssistant.dto;

import java.time.LocalDateTime;

public record StudySessionNameDTO (
        Integer id,
        String sessionName,
        LocalDateTime createdAt){}
