package com.ibgs.studyAssistant.studyCalendar.service;

import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.studyCalendar.domain.StudyDay;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayResponse;
import com.ibgs.studyAssistant.studyCalendar.repository.StudyDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyDayService {

    private final StudyDayRepository repository;
    private final UserService userService;

    public StudyDayResponse create(StudyDayRequest request){
        StudyDay studyDay = new StudyDay();

        User user = userService.findById(request.userId());

        studyDay.setUser(user);

        studyDay.setStudiedMinutes(request.studiedMinutes());
        studyDay.setStudyDate(request.studyDate());
        studyDay.setCompleted(request.completed());
        studyDay.setCompletedAt(request.completedAt());

        repository.save(studyDay);

        return new StudyDayResponse(
                studyDay.getId(),
                request.userId(),
                request.studyDate(),
                request.studiedMinutes(),
                request.completed(),
                request.completedAt()
        );
    }

    public void update(){
        //TODO método para atualizar dias estudados
    }

    public void findDaysByUser(){
        //TODO método para listar dias estudados
    }
}
