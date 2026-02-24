package com.ibgs.studyAssistant.studyCalendar.dto.pause;

import java.time.LocalDateTime;

public record PauseRequest(
        LocalDateTime startPause,
        LocalDateTime endPause,

        Integer studyDayId
) {
}
