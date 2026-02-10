package com.ibgs.studyAssistant.studyCalendar.dto.studyGoal;

public record StudyGoalRequest(
        Integer userId,

        Integer dailyStudyMinutes
) {
}
