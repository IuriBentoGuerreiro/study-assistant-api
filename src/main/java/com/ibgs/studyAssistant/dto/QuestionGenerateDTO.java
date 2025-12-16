package com.ibgs.studyAssistant.dto;

import java.util.List;

public record QuestionGenerateDTO(
        String statement,
        List<String> options,
        Integer correctAnswerIndex
) {}
