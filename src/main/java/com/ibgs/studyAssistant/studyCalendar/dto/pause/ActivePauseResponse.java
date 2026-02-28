package com.ibgs.studyAssistant.studyCalendar.dto.pause;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivePauseResponse(
        UUID id,
        LocalDateTime startTime
) {
}