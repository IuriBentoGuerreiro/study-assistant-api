package com.ibgs.studyAssistant.dto;

import java.util.List;

public record QuestionResponse(

        Integer id,

        String statement,

        List<String> options

) {}
