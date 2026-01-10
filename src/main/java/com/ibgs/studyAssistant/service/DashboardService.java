package com.ibgs.studyAssistant.service;

import com.ibgs.studyAssistant.auth.dto.AuthMeResponse;
import com.ibgs.studyAssistant.auth.service.AuthService;
import com.ibgs.studyAssistant.dto.DashboardDTO;
import com.ibgs.studyAssistant.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AuthService authService;
    private final QuestionRepository questionRepository;

    public DashboardDTO dashboard() {

        AuthMeResponse user = authService.getCurrentUser();

        long totalQuestions =
                questionRepository.countByUserId(user.id());

        long correctQuestions =
                questionRepository.countCorrectQuestionsByUser(user.id());

        double accuracyPercentage =
                totalQuestions == 0
                        ? 0.0
                        : (correctQuestions * 100.0) / totalQuestions;

        return new DashboardDTO(
                totalQuestions,
                correctQuestions,
                accuracyPercentage
        );
    }
    }
