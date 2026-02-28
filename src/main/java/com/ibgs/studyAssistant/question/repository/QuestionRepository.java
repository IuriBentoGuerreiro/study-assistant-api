package com.ibgs.studyAssistant.question.repository;

import com.ibgs.studyAssistant.question.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    @Query("""
    SELECT COUNT(q)
    FROM Question q
    JOIN q.studySession s
    WHERE s.user.id = :userId
""")
    long countByUserId(UUID userId);

    @Query("""
    SELECT COUNT(q)
    FROM Question q
    JOIN q.studySession s
    WHERE s.user.id = :userId
      AND q.studyAnswer IS NOT NULL
      AND q.studyAnswer = q.correctAnswerIndex
""")
    long countCorrectQuestionsByUser(UUID userId);

    List<Question> findByStudySessionId(UUID studySessionId);
}
