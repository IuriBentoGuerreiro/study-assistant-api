package com.ibgs.studyAssistant.studyCalendar.dto.pause;

import java.time.LocalDateTime;

public record ActivePauseResponse(
        Integer id,
        LocalDateTime startTime
) {
}