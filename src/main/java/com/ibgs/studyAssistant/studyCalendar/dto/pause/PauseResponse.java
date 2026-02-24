package com.ibgs.studyAssistant.studyCalendar.dto.pause;

import java.time.LocalDateTime;

public record PauseResponse(
        Integer id,
        LocalDateTime startPause,
        LocalDateTime endPause,

        Integer studyDayId

) {
}
