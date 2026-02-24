package com.ibgs.studyAssistant.studyCalendar.service;

import com.ibgs.studyAssistant.studyCalendar.domain.Pause;
import com.ibgs.studyAssistant.studyCalendar.dto.pause.PauseResponse;
import com.ibgs.studyAssistant.studyCalendar.mapper.PauseMapper;
import com.ibgs.studyAssistant.studyCalendar.repository.PauseRepository;
import com.ibgs.studyAssistant.studyCalendar.repository.StudyDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PauseService {

    private final PauseRepository repository;
    private final PauseMapper mapper;
    private final StudyDayRepository studyDayRepository;

    @Transactional
    public PauseResponse initPause(Integer studyDayId) {

        if (repository.existsByStudyDayIdAndEndPauseIsNull(studyDayId)) {
            throw new RuntimeException("Já existe uma pausa ativa.");
        }

        Pause pause = new Pause();
        pause.setStudyDay(studyDayRepository.getReferenceById(studyDayId));
        pause.setStartPause(LocalDateTime.now());

        Pause saved = repository.save(pause);

        return mapper.toResponse(saved);
    }

    @Transactional
    public void finishPause(Integer id) {

        Pause pause = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pausa não encontrada"));

        if (pause.getEndPause() != null) {
            throw new RuntimeException("Pausa já finalizada.");
        }

        pause.setEndPause(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<Pause> findByStudyDayId(Integer studyDayId){
        return repository.findByStudyDayId(studyDayId);
    }
}
