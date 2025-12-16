package com.ibgs.studyAssistant.repository;

import com.ibgs.studyAssistant.domain.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudySessionRepository extends JpaRepository<StudySession, Integer> {
}
