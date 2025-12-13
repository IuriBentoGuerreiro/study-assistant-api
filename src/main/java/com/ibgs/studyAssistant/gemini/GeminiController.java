package com.ibgs.studyAssistant.gemini;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ia")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/gerar")
    public ResponseEntity<String> gerar(@RequestBody String prompt) {
        String response = geminiService.generateQuestions(prompt);
        return ResponseEntity.ok(response);
    }
}
