package com.ibgs.studyAssistant.resume.dto;

import java.time.LocalDateTime;

public record ResumeTitleDTO (Integer id,
                              String title,
                              LocalDateTime createdAt){
}
