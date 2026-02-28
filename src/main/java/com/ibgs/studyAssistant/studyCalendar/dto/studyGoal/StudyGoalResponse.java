package com.ibgs.studyAssistant.studyCalendar.dto.studyGoal;

import java.util.UUID;

public record StudyGoalResponse(
        UUID id,

        UUID userId,

        Long dailyStudySeconds
) {
}
