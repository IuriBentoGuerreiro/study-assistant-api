package com.ibgs.studyAssistant.studyCalendar.service;

import com.ibgs.studyAssistant.auth.dto.UserMeResponse;
import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.studyCalendar.domain.StudyDay;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayResponse;
import com.ibgs.studyAssistant.studyCalendar.mapper.StudyDayMapper;
import com.ibgs.studyAssistant.studyCalendar.repository.StudyDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyDayService {

    private final StudyDayRepository repository;
    private final UserService userService;
    private final StudyDayMapper mapper;

    @Transactional
    public StudyDayResponse create(){

        if (findActiveSession() != null){
            throw new RuntimeException("Já existe uma sessão ativa.");
        }

        UserMeResponse user = userService.getCurrentUser();

        StudyDay studyDay = new StudyDay();

        studyDay.setUser(userService.getReference(user.id()));
        studyDay.setStartTime(LocalDateTime.now());
        studyDay.setEndTime(null);
        studyDay.setStudiedMinutes(0);
        studyDay.setActive(true);
        studyDay.setStudyDate(LocalDate.now());

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
    public List<StudyDayResponse> findByUser(LocalDate start, LocalDate end){
        Integer userId = userService.getCurrentUser().id();

        LocalDate startDate = (start != null) ? start : LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = (end != null) ? end : LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<StudyDay> studyDays = repository.findByUserIdAndStudyDateBetweenOrderByStudyDateAsc(userId, startDate, endDate);

        return mapper.toResponse(studyDays);
    }

    @Transactional(readOnly = true)
    public StudyDayResponse findActiveSession() {

        return repository.findByActive(true).map(mapper::toResponse).orElse(null);
    }

    @Transactional
    public StudyDayResponse finishSession(Integer id){
        StudyDay studyDay = repository.findById(id).orElseThrow(
                ()-> new RuntimeException("Dia de estudos não encontrado.")
        );

        studyDay.setEndTime(LocalDateTime.now());

        long minutes = ChronoUnit.MINUTES.between(studyDay.getStartTime(), studyDay.getEndTime());
        studyDay.setStudiedMinutes((int) minutes);

        studyDay.setActive(false);

        return mapper.toResponse(studyDay);
    }
}
