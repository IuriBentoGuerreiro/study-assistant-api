package com.ibgs.studyAssistant.service;

import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.domain.Question;
import com.ibgs.studyAssistant.domain.StudySession;
import com.ibgs.studyAssistant.dto.QuestionGenerateDTO;
import com.ibgs.studyAssistant.gemini.GeminiService;
import com.ibgs.studyAssistant.repository.StudySessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudySessionService {

    private final StudySessionRepository studySessionRepository;
    private final GeminiService geminiService;
    private final UserService userService;

    @Transactional
    public StudySession criarSessaoComQuestoes(
            Integer userId,
            List<QuestionGenerateDTO> questoesGeradas
    ) {
        StudySession session = new StudySession();
        session.setUser(userService.findById(userId));

        List<Question> questions = questoesGeradas.stream()
                .map(dto -> {
                    Question q = new Question();
                    q.setStatement(dto.statement());
                    q.setOptions(dto.options());
                    q.setStudySession(session);
                    return q;
                })
                .toList();

        session.setQuestions(questions);

        return studySessionRepository.save(session);
    }

    @Transactional
    public StudySession criarSessaoComIA(Integer userId, String prompt) {

        List<QuestionGenerateDTO> generated =
                geminiService.generateQuestions(prompt);

        StudySession session = new StudySession();
        session.setUser(userService.findById(userId));

        List<Question> questions = generated.stream().map(questionGenerateDTO -> {

                    QuestionGenerateDTO q = questionGenerateDTO;

                    Question entity = new Question();
                    entity.setStatement(q.statement());
                    entity.setOptions(q.options());
                    entity.setStudySession(session);

                    return entity;
                })
                .toList();

        session.setQuestions(questions);

        return studySessionRepository.save(session);
    }

}

