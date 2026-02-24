package com.ibgs.studyAssistant.studyCalendar.repository;

import com.ibgs.studyAssistant.studyCalendar.domain.Pause;
import com.ibgs.studyAssistant.studyCalendar.domain.StudyDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PauseRepository extends JpaRepository<Pause, Integer> {

    boolean existsByStudyDayIdAndEndPauseIsNull(Integer studyDayId);

    List<Pause> findByStudyDayId(Integer studyDayId);
}
