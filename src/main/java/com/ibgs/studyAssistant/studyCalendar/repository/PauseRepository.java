package com.ibgs.studyAssistant.studyCalendar.repository;

import com.ibgs.studyAssistant.studyCalendar.domain.Pause;
import com.ibgs.studyAssistant.studyCalendar.domain.StudyDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PauseRepository extends JpaRepository<Pause, UUID> {

    boolean existsByStudyDayIdAndEndPauseIsNull(UUID studyDayId);

    List<Pause> findByStudyDayId(UUID studyDayId);
}
