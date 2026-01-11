package com.ibgs.studyAssistant.service;

import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.domain.Question;
import com.ibgs.studyAssistant.domain.StudySession;
import com.ibgs.studyAssistant.dto.QuestionGenerateDTO;
import com.ibgs.studyAssistant.dto.StudySessionNameDTO;
import com.ibgs.studyAssistant.gemini.GeminiService;
import com.ibgs.studyAssistant.repository.StudySessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudySessionService {

    private final StudySessionRepository studySessionRepository;
    private final GeminiService geminiService;
    private final UserService userService;

    @Transactional
    public List<StudySessionNameDTO> findAllSessionNameByUser(Integer userId){
        return studySessionRepository.findSessionNameByUserId(userId);
    }

    @Transactional
    public StudySession findById(Integer id){
        return studySessionRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Recurso Não Encontrado")
        );
    }

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
    public StudySession criarSessaoComIA(Integer userId, String prompt, String banca, int quantidade) {

        List<QuestionGenerateDTO> generated =
                geminiService.generateQuestions(prompt, banca, quantidade);

        StudySession session = new StudySession();

        session.setSessionName(generateSessionName(prompt));

        session.setUser(userService.findById(userId));


        List<Question> questions = generated.stream().map(questionGenerateDTO -> {

                    QuestionGenerateDTO q = questionGenerateDTO;

                    Question entity = new Question();
                    entity.setStatement(q.statement());
                    entity.setOptions(q.options());
                    entity.setStudySession(session);
                    entity.setCorrectAnswerIndex(q.correctAnswerIndex());

                    return entity;
                })
                .toList();

        session.setQuestions(questions);

        return studySessionRepository.save(session);
    }

    private String generateSessionName(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return "Nova sessão de estudo";
        }

        String extracted = extractPromptIfJson(prompt);

        if (extracted.isBlank()) {
            return "Nova sessão de estudo";
        }

        String cleaned = extracted
                .trim()
                .replaceAll("\\s+", " ")
                .replaceAll("[\\r\\n{}\"]", "");

        String[] words = cleaned.split(" ");

        String title = words.length <= 6
                ? cleaned
                : String.join(" ", Arrays.copyOfRange(words, 0, 6));

        return capitalize(title);
    }

    private String extractPromptIfJson(String input) {
        input = input.trim();

        if (input.startsWith("{") && input.endsWith("}")) {
            int idx = input.indexOf("\"prompt\"");
            if (idx != -1) {
                int start = input.indexOf(":", idx) + 1;
                int end = input.lastIndexOf("\"");
                if (start > 0 && end > start) {
                    return input.substring(start, end).replaceAll("\"", "").trim();
                }
            }
            return "";
        }

        return input;
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
