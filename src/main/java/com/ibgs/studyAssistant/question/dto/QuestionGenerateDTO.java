package com.ibgs.studyAssistant.question.dto;

import com.ibgs.studyAssistant.question.enuns.QuestionType;

import java.util.List;

public record QuestionGenerateDTO(
        QuestionType type,
        String statement,
        List<String> options,
        Integer correctAnswerIndex,
        String comment
) {
}
