package com.ibgs.studyAssistant.question.dto;

import java.util.List;
import java.util.UUID;

public record QuestionResponse(

        UUID id,
        String statement,
        List<String> options,
        Integer correctAnswerIndex,
        Integer studyAnswer,
        String comment
) {}
