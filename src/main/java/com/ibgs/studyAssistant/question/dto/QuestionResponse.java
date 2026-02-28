package com.ibgs.studyAssistant.question.dto;

import com.ibgs.studyAssistant.question.enuns.QuestionType;

import java.util.List;
import java.util.UUID;

public record QuestionResponse(

        UUID id,
        String statement,
        QuestionType type,
        List<String> options,
        Integer correctAnswerIndex,
        Integer studyAnswer

) {}
