package com.ibgs.studyAssistant.studyCalendar.repository;

import com.ibgs.studyAssistant.studyCalendar.domain.StudyDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudyDayRepository extends JpaRepository<StudyDay, UUID> {

    List<StudyDay> findByUserIdAndStudyDateBetweenOrderByStudyDateAsc(UUID userId, LocalDate startDate, LocalDate endDate);

    Optional<StudyDay> findByActive(Boolean active);
}
