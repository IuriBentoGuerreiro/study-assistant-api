package com.ibgs.studyAssistant.gemini;

import com.ibgs.studyAssistant.dto.QuestionGenerateDTO;
import com.ibgs.studyAssistant.dto.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ia")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/gerar")
    public ResponseEntity<List<QuestionGenerateDTO>> gerar(
            @RequestBody String prompt
    ) {
        List<QuestionGenerateDTO> response =
                geminiService.generateQuestions(prompt);

        return ResponseEntity.ok(response);
    }
}
