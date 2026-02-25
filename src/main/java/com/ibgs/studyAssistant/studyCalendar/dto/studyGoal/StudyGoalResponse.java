package com.ibgs.studyAssistant.studyCalendar.dto.studyGoal;

public record StudyGoalResponse(
        Integer id,

        Integer userId,

        Long dailyStudySeconds
) {
}
