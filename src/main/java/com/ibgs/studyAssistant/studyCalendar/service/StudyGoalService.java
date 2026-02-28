package com.ibgs.studyAssistant.studyCalendar.service;

import com.ibgs.studyAssistant.auth.dto.UserMeResponse;
import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.studyCalendar.domain.StudyGoal;
import com.ibgs.studyAssistant.studyCalendar.dto.studyGoal.StudyGoalRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyGoal.StudyGoalResponse;
import com.ibgs.studyAssistant.studyCalendar.mapper.StudyGoalMapper;
import com.ibgs.studyAssistant.studyCalendar.repository.StudyGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudyGoalService {

    private final StudyGoalRepository repository;
    private final UserService userService;
    private final StudyGoalMapper mapper;

    @Transactional
    public StudyGoalResponse create(StudyGoalRequest request) {

        UUID userId = userService.getCurrentUser().id();

        StudyGoal studyGoal = mapper.toEntity(request);
        studyGoal.setUser(userService.findById(userId));

        repository.save(studyGoal);

        return mapper.toResponse(studyGoal);
    }

    @Transactional
    public StudyGoalResponse update(UUID id, StudyGoalRequest request){
        StudyGoal studyGoal = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Meta não encontrada")
        );

        BeanUtils.copyProperties(request, studyGoal, "id");

        return mapper.toResponse(repository.save(studyGoal));
    }

    @Transactional(readOnly = true)
    public StudyGoalResponse findByUser() {
        UserMeResponse user = userService.getCurrentUser();

        return repository.findByUserId(user.id())
                .map(mapper::toResponse)
                .orElseGet(() -> new StudyGoalResponse(
                        null,
                        user.id(),
                        3600L
                ));
    }}
