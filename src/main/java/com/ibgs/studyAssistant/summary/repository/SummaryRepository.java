package com.ibgs.studyAssistant.summary.repository;

import com.ibgs.studyAssistant.summary.domain.Summary;
import com.ibgs.studyAssistant.summary.dto.SummaryTitleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SummaryRepository extends JpaRepository<Summary, UUID> {

    @Query("""
    select new com.ibgs.studyAssistant.summary.dto.SummaryTitleDTO(
        s.id,
        s.title,
        s.createdAt
    )
    from Summary s
    where s.user.id = :userId
""")
    List<SummaryTitleDTO> findSummaryByUserId(@Param("userId") UUID userId);
}
