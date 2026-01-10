package com.ibgs.studyAssistant.dto;

public record DashboardDTO(
        long questionsGenerated,
        long correctQuestions,
        double accuracyPercentage
)
{}
