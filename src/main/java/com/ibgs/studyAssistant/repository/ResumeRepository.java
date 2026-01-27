package com.ibgs.studyAssistant.repository;

import com.ibgs.studyAssistant.domain.Resume;
import com.ibgs.studyAssistant.dto.ResumeTitleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Integer> {

    @Query("""
    select new com.ibgs.studyAssistant.dto.ResumeTitleDTO(
        r.id,
        r.title,
        r.createdAt
    )
    from Resume r
    where r.user.id = :userId
""")
    List<ResumeTitleDTO> findResumeByUserId(@Param("userId") Integer userId);
}
