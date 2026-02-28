package com.ibgs.studyAssistant.question.service;

import com.ibgs.studyAssistant.question.domain.Question;
import com.ibgs.studyAssistant.question.dto.UserAnswerDTO;
import com.ibgs.studyAssistant.question.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public List<Question> findAllBySession(UUID studySessionId){

        return questionRepository.findByStudySessionId(studySessionId);
    }

    @Transactional
    public void questionUserResponse (UserAnswerDTO userAnswerDTO){
        Question question = questionRepository.findById(userAnswerDTO.questionId()).orElseThrow(
                () -> new RuntimeException("Questão Não Encontrada")
        );

        question.setStudyAnswer(userAnswerDTO.selectedOptionIndex());

        questionRepository.save(question);
    }
}
