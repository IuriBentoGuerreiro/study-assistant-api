package com.ibgs.studyAssistant.studyCalendar.dto.pause;

import java.time.LocalDateTime;
import java.util.UUID;

public record PauseResponse(
        UUID id,
        LocalDateTime startPause,
        LocalDateTime endPause,

        Integer studyDayId

) {
}
