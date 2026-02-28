package com.ibgs.studyAssistant.studyCalendar.dto.studyDay;

import com.ibgs.studyAssistant.studyCalendar.dto.pause.ActivePauseResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record StudyDayResponse(
        UUID id,

        UUID userId,

        String description,
        LocalDate studyDate,
        Long studiedSeconds,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Boolean active,
        Long totalPausedSeconds,
        ActivePauseResponse activePause
) {
}
