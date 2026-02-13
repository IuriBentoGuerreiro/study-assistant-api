package com.ibgs.studyAssistant.studyCalendar.repository;

import com.ibgs.studyAssistant.studyCalendar.domain.StudyDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyDayRepository extends JpaRepository<StudyDay, Integer> {

    List<StudyDay> findByUserId(Integer UserId);

    Optional<StudyDay> findByActive(Boolean active);
}
