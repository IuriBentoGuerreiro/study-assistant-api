package com.ibgs.studyAssistant.dto;

import java.time.LocalDateTime;

public record ResumeTitleDTO (Integer id,
                              String title,
                              LocalDateTime createdAt){
}
