package com.ibgs.studyAssistant.studyCalendar.dto.studyDay;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StudyDayManualRequest(
        String description,
        LocalDate studyDate,
        Integer studiedMinutes,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
