package com.ibgs.studyAssistant.studySession.dto;

public record PromptRequest(

        String prompt,
        Integer quantidade,
        String nivel
) {}