package com.ibgs.studyAssistant.service;

import com.ibgs.studyAssistant.domain.Question;
import com.ibgs.studyAssistant.domain.StudySession;
import com.ibgs.studyAssistant.dto.QuestionResponse;
import com.ibgs.studyAssistant.dto.UserAnswerDTO;
import com.ibgs.studyAssistant.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final StudySessionService studySessionService;

    @Transactional
    public List<Question> findAllBySession(Integer studySessionId){
        StudySession session = studySessionService.findById(studySessionId);

        return session.getQuestions();
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
