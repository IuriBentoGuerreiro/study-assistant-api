package com.ibgs.studyAssistant.repository;

import com.ibgs.studyAssistant.domain.StudySession;
import com.ibgs.studyAssistant.dto.StudySessionNameDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Integer> {

    @Query("""
    select new com.ibgs.studyAssistant.dto.StudySessionNameDTO(
        s.id,
        s.sessionName,
        s.createdAt
    )
    from StudySession s
    where s.user.id = :userId
""")
    List<StudySessionNameDTO> findSessionNameByUserId(@Param("userId") Integer userId);
}
