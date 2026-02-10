package com.ibgs.studyAssistant.studyCalendar.dto.studyDay;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StudyDayResponse(
        Integer id,

        Integer userId,

        LocalDate studyDate,
        Integer studiedMinutes,
        Boolean completed,
        LocalDateTime completedAt
) {
}
