package com.ibgs.studyAssistant.repository;

import com.ibgs.studyAssistant.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
