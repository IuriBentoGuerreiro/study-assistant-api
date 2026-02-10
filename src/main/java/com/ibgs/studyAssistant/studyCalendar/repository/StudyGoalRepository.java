package com.ibgs.studyAssistant.studyCalendar.repository;

import com.ibgs.studyAssistant.studyCalendar.domain.StudyGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyGoalRepository extends JpaRepository<StudyGoal, Integer> {

    Optional<StudyGoal> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);
}
