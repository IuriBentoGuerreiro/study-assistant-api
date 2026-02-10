package com.ibgs.studyAssistant.question.enuns;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum QuestionType {
    MULTIPLE_CHOICE,
    TRUE_FALSE;

    @JsonCreator
    public static QuestionType from(String value) {
        return QuestionType.valueOf(
                value.trim().toUpperCase().replace(" ", "_")
        );
    }
}
