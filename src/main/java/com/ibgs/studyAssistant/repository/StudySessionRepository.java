package com.ibgs.studyAssistant.repository;

import com.ibgs.studyAssistant.domain.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Integer> {

    List<StudySession> findByUserId(Integer userId);
}
