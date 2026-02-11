package com.ibgs.studyAssistant.studyCalendar.service;

import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.studyCalendar.domain.StudyGoal;
import com.ibgs.studyAssistant.studyCalendar.dto.studyGoal.StudyGoalRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyGoal.StudyGoalResponse;
import com.ibgs.studyAssistant.studyCalendar.mapper.StudyGoalMapper;
import com.ibgs.studyAssistant.studyCalendar.repository.StudyGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGoalService {

    private final StudyGoalRepository repository;
    private final UserService userService;
    private final StudyGoalMapper mapper;

    public StudyGoalResponse createOrUpdate(StudyGoalRequest request) {

        User user = userService.findById(request.userId());

        StudyGoal studyGoal = mapper.toEntity(request);
        studyGoal.setUser(user);

        repository.save(studyGoal);

        return mapper.toResponse(studyGoal);
    }

    public StudyGoalResponse findByUser(Integer userId) {
        StudyGoal studyGoal = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Meta de estudos não encontrada"));

        return mapper.toResponse(studyGoal);
    }
}
