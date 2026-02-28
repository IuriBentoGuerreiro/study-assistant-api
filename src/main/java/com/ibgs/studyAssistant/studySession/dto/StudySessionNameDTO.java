package com.ibgs.studyAssistant.studySession.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record StudySessionNameDTO (
        UUID id,
        String sessionName,
        LocalDateTime createdAt){}
