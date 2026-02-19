package com.ibgs.studyAssistant.studyCalendar.dto.studyDay;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StudyDayResponse(
        Integer id,

        Integer userId,

        String description,
        LocalDate studyDate,
        Long studiedSeconds,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Boolean active
) {
}
