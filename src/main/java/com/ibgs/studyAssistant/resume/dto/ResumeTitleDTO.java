package com.ibgs.studyAssistant.resume.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResumeTitleDTO (UUID id,
                              String title,
                              LocalDateTime createdAt){
}
