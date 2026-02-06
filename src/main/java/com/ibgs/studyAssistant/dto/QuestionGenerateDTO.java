package com.ibgs.studyAssistant.dto;

import com.ibgs.studyAssistant.domain.QuestionOption;
import com.ibgs.studyAssistant.enuns.QuestionType;

import java.util.List;

public record QuestionGenerateDTO(
        QuestionType type,
        String statement,
        List<String> options,
        Integer correctAnswerIndex
) {
}
