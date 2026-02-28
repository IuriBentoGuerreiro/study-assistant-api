package com.ibgs.studyAssistant.studySession.repository;

import com.ibgs.studyAssistant.studySession.domain.StudySession;
import com.ibgs.studyAssistant.studySession.dto.StudySessionNameDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StudySessionRepository extends JpaRepository<StudySession, UUID> {

    @Query("""
    select new com.ibgs.studyAssistant.studySession.dto.StudySessionNameDTO(
        s.id,
        s.sessionName,
        s.createdAt
    )
    from StudySession s
    where s.user.id = :userId
""")
    List<StudySessionNameDTO> findSessionNameByUserId(@Param("userId") UUID userId);
}
