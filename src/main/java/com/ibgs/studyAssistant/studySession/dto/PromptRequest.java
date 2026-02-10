package com.ibgs.studyAssistant.studySession.dto;

import com.ibgs.studyAssistant.question.enuns.QuestionType;

public record PromptRequest(

        String prompt,
        String banca,
        Integer quantidade,
        QuestionType type,
        String orgao,
        String cargo,
        String cidade,
        String estado,
        String nivel
) {}