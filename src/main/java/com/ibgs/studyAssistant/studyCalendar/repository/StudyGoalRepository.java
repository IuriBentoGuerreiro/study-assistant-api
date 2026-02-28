package com.ibgs.studyAssistant.studyCalendar.repository;

import com.ibgs.studyAssistant.studyCalendar.domain.StudyGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudyGoalRepository extends JpaRepository<StudyGoal, UUID> {

    Optional<StudyGoal> findByUserId(UUID userId);
}
