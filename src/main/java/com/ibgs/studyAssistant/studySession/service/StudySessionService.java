package com.ibgs.studyAssistant.studySession.service;

import com.ibgs.studyAssistant.auth.dto.UserMeResponse;
import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.exception.LimitExceededException;
import com.ibgs.studyAssistant.gemini.GeminiService;
import com.ibgs.studyAssistant.question.domain.Question;
import com.ibgs.studyAssistant.question.domain.QuestionOption;
import com.ibgs.studyAssistant.question.dto.QuestionGenerateDTO;
import com.ibgs.studyAssistant.question.dto.QuestionResponse;
import com.ibgs.studyAssistant.question.mapper.QuestionMapper;
import com.ibgs.studyAssistant.question.service.QuestionService;
import com.ibgs.studyAssistant.studySession.domain.StudySession;
import com.ibgs.studyAssistant.studySession.dto.PromptRequest;
import com.ibgs.studyAssistant.studySession.dto.StudySessionNameDTO;
import com.ibgs.studyAssistant.studySession.dto.StudySessionResponseDTO;
import com.ibgs.studyAssistant.studySession.repository.StudySessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudySessionService {

    private final StudySessionRepository studySessionRepository;
    private final GeminiService geminiService;
    private final UserService userService;
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    @Transactional(readOnly = true)
    public List<StudySessionNameDTO> findAllSessionNameByUser() {
        UserMeResponse user = userService.getCurrentUser();

        return studySessionRepository.findSessionNameByUserId(user.id());
    }

    @Transactional(readOnly = true)
    public StudySessionResponseDTO findFullSession(UUID sessionId) {
        StudySession session = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada"));

        List<Question> questions = questionService.findAllBySession(sessionId);

        List<QuestionResponse> response = questionMapper.toResponse(questions);

        return new StudySessionResponseDTO(
                session.getId(),
                session.getSessionName(),
                response
        );
    }

    @Transactional
    public StudySessionResponseDTO generateSession(PromptRequest request) {

        if (request.quantidade() > 50) {
            throw new LimitExceededException
                    ("O Limite de questões a ser gerado por vez é 50");
        }

        UserMeResponse user = userService.getCurrentUser();

        List<QuestionGenerateDTO> generated =
                geminiService.generateQuestions(request);

        StudySession session = new StudySession();

        session.setSessionName(generateSessionName(request.prompt()));

        session.setUser(userService.findById(user.id()));

        List<Question> questions = generated.stream().map(q -> {

            Question question = new Question();
            question.setStatement(q.statement());
            question.setCorrectAnswerIndex(q.correctAnswerIndex());
            question.setType(q.type());
            question.setStudySession(session);
            question.setComment(q.comment());

            List<QuestionOption> options =
                    q.options() == null
                            ? List.of()
                            : q.options().stream()
                            .map(opt -> {
                                QuestionOption option = new QuestionOption();
                                option.setOptions(opt);
                                option.setQuestion(question);
                                return option;
                            })
                            .toList();

            question.setOptions(options);

            return question;
        }).toList();

        session.setQuestions(questions);

        StudySession saved = studySessionRepository.save(session);

        return new StudySessionResponseDTO(
                saved.getId(),
                saved.getSessionName(),
                saved.getQuestions().stream().map(q ->
                        new QuestionResponse(
                                q.getId(),
                                q.getStatement(),
                                q.getType(),
                                q.getOptions() == null
                                        ? List.of()
                                        : q.getOptions().stream()
                                        .map(QuestionOption::getOptions)
                                        .toList(),
                                q.getCorrectAnswerIndex(),
                                q.getStudyAnswer(),
                                q.getComment()
                        )
                ).toList()
        );

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

    @Transactional
    public void delete(UUID id) {
        studySessionRepository.deleteById(id);
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
