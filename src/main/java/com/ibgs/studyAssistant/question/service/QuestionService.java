package com.ibgs.studyAssistant.question.service;

import com.ibgs.studyAssistant.question.domain.Question;
import com.ibgs.studyAssistant.question.repository.QuestionRepository;
import com.ibgs.studyAssistant.studySession.domain.StudySession;
import com.ibgs.studyAssistant.question.dto.UserAnswerDTO;
import com.ibgs.studyAssistant.studySession.service.StudySessionService;
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
