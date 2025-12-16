package com.ibgs.studyAssistant.dto;

import java.util.List;

public record QuestionRequest(

        String statement,

        List<String> options,

        Integer correctAnswerIndex

) {}