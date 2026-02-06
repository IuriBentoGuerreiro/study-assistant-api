package com.ibgs.studyAssistant.dto;

import com.ibgs.studyAssistant.enuns.QuestionType;

import java.util.List;

public record QuestionResponse(

        Integer id,
        String statement,
        QuestionType type,
        List<String> options,
        Integer correctAnswerIndex,
        Integer studyAnswer

) {}
