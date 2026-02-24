package com.ibgs.studyAssistant.studyCalendar.service;

import com.ibgs.studyAssistant.auth.dto.UserMeResponse;
import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.studyCalendar.domain.Pause;
import com.ibgs.studyAssistant.studyCalendar.domain.StudyDay;
import com.ibgs.studyAssistant.studyCalendar.dto.pause.ActivePauseResponse;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayDescriptionRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayManualRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayResponse;
import com.ibgs.studyAssistant.studyCalendar.mapper.StudyDayMapper;
import com.ibgs.studyAssistant.studyCalendar.repository.StudyDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
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
    private final PauseService pauseService;

    @Transactional
    public StudyDayResponse create(StudyDayDescriptionRequest studyDayDescription){

        if (findActiveSession() != null){
            throw new RuntimeException("Já existe uma sessão ativa.");
        }

        UserMeResponse user = userService.getCurrentUser();

        StudyDay studyDay = new StudyDay();

        studyDay.setDescription(studyDayDescription.description());
        studyDay.setUser(userService.getReference(user.id()));
        studyDay.setStartTime(LocalDateTime.now());
        studyDay.setEndTime(null);
        studyDay.setStudiedSeconds(0L);
        studyDay.setActive(true);
        studyDay.setStudyDate(LocalDate.now());

        repository.save(studyDay);

        return mapper.toResponse(studyDay);
    }

    @Transactional
    public StudyDayResponse createManual(StudyDayManualRequest request) {
        UserMeResponse user = userService.getCurrentUser();
        StudyDay studyDay = new StudyDay();

        studyDay.setDescription(request.description());
        studyDay.setUser(userService.getReference(user.id()));
        studyDay.setStudyDate(request.studyDate());

        studyDay.setStartTime(request.startTime());
        studyDay.setEndTime(request.endTime());
        studyDay.setStudiedSeconds(request.studiedSeconds());

        studyDay.setActive(false);
        repository.save(studyDay);

        return mapper.toResponse(studyDay);
    }

    @Transactional
    public StudyDayResponse update(Integer id, StudyDayRequest request) {
        StudyDay studyDay = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("sessão de estudo não encontrada"));

        BeanUtils.copyProperties(request, studyDay, "id");

        studyDay.setActive(studyDay.getEndTime() == null);

        if (studyDay.getEndTime() != null) {
            long seconds = ChronoUnit.SECONDS.between(studyDay.getStartTime(), studyDay.getEndTime());
            studyDay.setStudiedSeconds(seconds);
        }

        var saved = repository.save(studyDay);

        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<StudyDayResponse> findByUser(LocalDate start, LocalDate end){
        Integer userId = userService.getCurrentUser().id();

        LocalDate startDate = (start != null) ? start : LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = (end != null) ? end : LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<StudyDay> studyDays = repository.findByUserIdAndStudyDateBetweenOrderByStudyDateAsc(userId, startDate, endDate);

        return mapper.toResponse(studyDays);
    }

    public void deleteById(Integer id){
        StudyDay studyDay = repository.findById(id).orElseThrow(
                () -> new RuntimeException("sessão de estudo não encontrada")
        );

        repository.delete(studyDay);
    }
    @Transactional(readOnly = true)
    public StudyDayResponse findActiveSession() {
        return repository.findByActive(true).map(studyDay -> {
            List<Pause> pauses = pauseService.findByStudyDayId(studyDay.getId());

            long totalPausedSeconds = pauses.stream()
                    .filter(p -> p.getEndPause() != null)
                    .mapToLong(p -> Duration.between(p.getStartPause(), p.getEndPause()).getSeconds())
                    .sum();

            ActivePauseResponse activePause = pauses.stream()
                    .filter(p -> p.getEndPause() == null)
                    .findFirst()
                    .map(p -> new ActivePauseResponse(p.getId(), p.getStartPause()))
                    .orElse(null);

            return new StudyDayResponse(
                    studyDay.getId(),
                    studyDay.getUser().getId(),
                    studyDay.getDescription(),
                    studyDay.getStudyDate(),
                    studyDay.getStudiedSeconds(),
                    studyDay.getStartTime(),
                    studyDay.getEndTime(),
                    studyDay.getActive(),
                    totalPausedSeconds,
                    activePause
            );
        }).orElse(null);
    }

    @Transactional
    public StudyDayResponse finishSession(Integer id) {
        StudyDay studyDay = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dia de estudos não encontrado."));

        LocalDateTime now = LocalDateTime.now();
        studyDay.setEndTime(now);

        studyDay.getPauses().stream()
                .filter(p -> p.getEndPause() == null)
                .forEach(p -> p.setEndPause(now));

        long totalSeconds = ChronoUnit.SECONDS.between(studyDay.getStartTime(), now);

        long totalPauseSeconds = studyDay.getPauses().stream()
                .mapToLong(p -> ChronoUnit.SECONDS.between(p.getStartPause(), p.getEndPause()))
                .sum();

        studyDay.setStudiedSeconds(Math.max(totalSeconds - totalPauseSeconds, 0));
        studyDay.setActive(false);

        return mapper.toResponse(studyDay);
    }}
