package com.ibgs.studyAssistant.summary.service;

import com.ibgs.studyAssistant.auth.dto.UserMeResponse;
import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.gemini.GeminiService;
import com.ibgs.studyAssistant.summary.domain.Summary;
import com.ibgs.studyAssistant.summary.dto.SummaryTitleDTO;
import com.ibgs.studyAssistant.summary.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final GeminiService geminiService;
    private final UserService userService;

    @Transactional
    public Summary findById(UUID id){
        return summaryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Recurso não encontrado")
        );
    }

    @Transactional
    public List<SummaryTitleDTO> findAllByUser() {
        UserMeResponse user = userService.getCurrentUser();

        return summaryRepository.findSummaryByUserId(user.id());
    }

    public Summary generateSumarry(String prompt) {
        String summaryText = geminiService.generateSummary(prompt);

        UserMeResponse user = userService.getCurrentUser();

        Summary summary = new Summary();

        summary.setText(summaryText);
        summary.setUser(userService.findById(user.id()));
        summary.setTitle(generateSummaryTitle(prompt));

        return summaryRepository.save(summary);
    }

    @Transactional
    public void delete(UUID id){
        summaryRepository.deleteById(id);
    }

    private String generateSummaryTitle(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return "Novo Resumo de estudo";
        }

        String extracted = extractPromptIfJson(prompt);

        if (extracted.isBlank()) {
            return "Novo Resumo de estudo";
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
