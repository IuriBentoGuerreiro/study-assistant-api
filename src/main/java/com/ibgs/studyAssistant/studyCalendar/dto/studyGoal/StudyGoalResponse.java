package com.ibgs.studyAssistant.studyCalendar.dto.studyGoal;

public record StudyGoalResponse(
        Integer id,

        Integer user,

        Long dailyStudySeconds
) {
}
