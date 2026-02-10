package com.ibgs.studyAssistant.studySession.dto;

import com.ibgs.studyAssistant.question.dto.QuestionResponse;

import java.util.List;

public record StudySessionResponseDTO(
        Integer id,
        String sessionName,
        List<QuestionResponse> questions
) {
}
