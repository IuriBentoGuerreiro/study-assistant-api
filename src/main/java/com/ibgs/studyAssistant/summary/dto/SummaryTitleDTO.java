package com.ibgs.studyAssistant.summary.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SummaryTitleDTO (UUID id,
                              String title,
                              LocalDateTime createdAt){
}
