package com.ibgs.studyAssistant.dto;

import java.util.List;

public record StudySessionResponseDTO(
        Integer id,
        String sessionName,
        List<QuestionResponse> questions
) {
}
