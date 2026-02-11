package com.ibgs.studyAssistant.studyCalendar.service;

import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.studyCalendar.mapper.StudyDayMapper;
import com.ibgs.studyAssistant.studyCalendar.domain.StudyDay;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayResponse;
import com.ibgs.studyAssistant.studyCalendar.repository.StudyDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyDayService {

    private final StudyDayRepository repository;
    private final UserService userService;
    private final StudyDayMapper mapper;

    @Transactional
    public StudyDayResponse create(StudyDayRequest request){
        User user = userService.findById(request.userId());

        StudyDay studyDay = mapper.toEntity(request);
        studyDay.setUser(user);

        repository.save(studyDay);

        return mapper.toResponse(studyDay);
    }

    @Transactional
    public StudyDayResponse update(Integer id, StudyDayRequest request){
        StudyDay studyDay = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Dia não encontrado")
        );

        BeanUtils.copyProperties(request, studyDay, "id");

        return mapper.toResponse(repository.save(studyDay));
    }

    @Transactional(readOnly = true)
    public List<StudyDayResponse> findByUser(Integer userId){
        List<StudyDay> studyDays = repository.findByUserId(userId);

        return mapper.toResponse(studyDays);
    }
}
