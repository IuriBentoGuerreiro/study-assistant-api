package com.ibgs.studyAssistant.studyCalendar.repository;

import com.ibgs.studyAssistant.studyCalendar.domain.StudyDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyDayRepository extends JpaRepository<StudyDay, Integer> {
}
