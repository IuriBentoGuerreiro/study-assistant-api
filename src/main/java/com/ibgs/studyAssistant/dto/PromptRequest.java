package com.ibgs.studyAssistant.dto;

import com.ibgs.studyAssistant.enuns.QuestionType;

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