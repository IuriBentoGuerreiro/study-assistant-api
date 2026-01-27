package com.ibgs.studyAssistant.service;

import com.ibgs.studyAssistant.auth.dto.AuthMeResponse;
import com.ibgs.studyAssistant.auth.service.AuthService;
import com.ibgs.studyAssistant.auth.service.UserService;
import com.ibgs.studyAssistant.domain.Resume;
import com.ibgs.studyAssistant.dto.ResumeTitleDTO;
import com.ibgs.studyAssistant.gemini.GeminiService;
import com.ibgs.studyAssistant.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final GeminiService geminiService;
    private final AuthService authService;
    private final UserService userService;

    @Transactional
    public Resume findById(Integer id){
        return resumeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Recurso não encontrado")
        );
    }

    @Transactional
    public List<ResumeTitleDTO> findAllByUser() {
        AuthMeResponse user = authService.getCurrentUser();

        return resumeRepository.findResumeByUserId(user.id());
    }

    public Resume generateResume(String prompt) {
        String resumeText = geminiService.generateResume(prompt);

        AuthMeResponse user = authService.getCurrentUser();

        Resume resume = new Resume();

        resume.setText(resumeText);
        resume.setUser(userService.findById(user.id()));
        resume.setTitle(generateResumeTitle(prompt));

        return resumeRepository.save(resume);
    }

    private String generateResumeTitle(String prompt) {
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
