package com.ibgs.studyAssistant.question.dto;

import java.util.UUID;

public record UserAnswerDTO(
        UUID questionId,
        Integer selectedOptionIndex)
{}
