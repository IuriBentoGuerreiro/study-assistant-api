package com.ibgs.studyAssistant.repository;

import com.ibgs.studyAssistant.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Query("""
    SELECT COUNT(q)
    FROM Question q
    JOIN q.studySession s
    WHERE s.user.id = :userId
""")
    long countByUserId(Integer userId);

    @Query("""
    SELECT COUNT(q)
    FROM Question q
    JOIN q.studySession s
    WHERE s.user.id = :userId
      AND q.studyAnswer IS NOT NULL
      AND q.studyAnswer = q.correctAnswerIndex
""")
    long countCorrectQuestionsByUser(Integer userId);
}
