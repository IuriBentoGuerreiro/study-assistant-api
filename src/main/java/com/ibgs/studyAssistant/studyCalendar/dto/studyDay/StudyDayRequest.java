package com.ibgs.studyAssistant.studyCalendar.dto.studyDay;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StudyDayRequest(

        String description,
        LocalDate studyDate,
        Integer studiedMinutes,
        Boolean completed,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Boolean active) {
}
