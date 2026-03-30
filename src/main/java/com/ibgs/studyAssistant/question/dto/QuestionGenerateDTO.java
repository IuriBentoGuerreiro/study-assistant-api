package com.ibgs.studyAssistant.question.dto;

import java.util.List;

public record QuestionGenerateDTO(
        String statement,
        List<String> options,
        Integer correctAnswerIndex,
        String comment
) {
}
