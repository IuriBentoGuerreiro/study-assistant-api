package com.ibgs.studyAssistant.studySession.dto;

import com.ibgs.studyAssistant.question.dto.QuestionResponse;

import java.util.List;
import java.util.UUID;

public record StudySessionResponseDTO(
        UUID id,
        String sessionName,
        List<QuestionResponse> questions
) {
}
