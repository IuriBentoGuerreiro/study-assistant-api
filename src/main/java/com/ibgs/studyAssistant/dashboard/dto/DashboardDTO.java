package com.ibgs.studyAssistant.dashboard.dto;

public record DashboardDTO(
        long questionsGenerated,
        long correctQuestions,
        double accuracyPercentage
)
{}
