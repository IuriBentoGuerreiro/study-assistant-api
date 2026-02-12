package com.ibgs.studyAssistant.dashboard.service;

import com.ibgs.studyAssistant.auth.dto.UserMeResponse;
import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.dashboard.dto.DashboardDTO;
import com.ibgs.studyAssistant.question.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserService userService;
    private final QuestionRepository questionRepository;

    @Transactional
    public DashboardDTO dashboard() {

        UserMeResponse user = userService.getCurrentUser();

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
