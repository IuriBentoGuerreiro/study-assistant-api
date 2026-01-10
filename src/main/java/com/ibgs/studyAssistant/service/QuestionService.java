package com.ibgs.studyAssistant.service;

import com.ibgs.studyAssistant.domain.Question;
import com.ibgs.studyAssistant.dto.UserAnswerDTO;
import com.ibgs.studyAssistant.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public void questionUserResponse (UserAnswerDTO userAnswerDTO){
        Question question = questionRepository.findById(userAnswerDTO.questionId()).orElseThrow(
                () -> new RuntimeException("Questão Não Encontrada")
        );

        question.setStudyAnswer(userAnswerDTO.selectedOptionIndex());

        questionRepository.save(question);
    }
}
