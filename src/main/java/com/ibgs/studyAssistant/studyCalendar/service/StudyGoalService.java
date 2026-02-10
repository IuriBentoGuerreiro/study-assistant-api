package com.ibgs.studyAssistant.studyCalendar.service;

import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.studyCalendar.domain.StudyGoal;
import com.ibgs.studyAssistant.studyCalendar.dto.studyGoal.StudyGoalRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyGoal.StudyGoalResponse;
import com.ibgs.studyAssistant.studyCalendar.repository.StudyGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGoalService {

    private final StudyGoalRepository repository;
    private final UserService userService;

    public StudyGoalResponse createOrUpdate(StudyGoalRequest request) {

        User user = userService.findById(request.userId());

        StudyGoal studyGoal = repository.findByUserId(user.getId())
                .orElseGet(StudyGoal::new);

        studyGoal.setUser(user);
        studyGoal.setDailyStudyMinutes(request.dailyStudyMinutes());

        repository.save(studyGoal);

        return new StudyGoalResponse(
                studyGoal.getId(),
                user.getId(),
                studyGoal.getDailyStudyMinutes()
        );
    }

    public StudyGoalResponse findByUser(Integer userId) {
        StudyGoal goal = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Meta de estudos não encontrada"));

        return new StudyGoalResponse(
                goal.getId(),
                userId,
                goal.getDailyStudyMinutes()
        );
    }
}
