package com.ibgs.studyAssistant.controller;

import com.ibgs.studyAssistant.domain.StudySession;
import com.ibgs.studyAssistant.dto.QuestionGenerateDTO;
import com.ibgs.studyAssistant.service.StudySessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class StudySessionController {

    private final StudySessionService studySessionService;

    @GetMapping("/userId")
    public ResponseEntity<List<StudySession>> findAllByUser(@PathVariable Integer userId){
        List<StudySession>  studySessions =  studySessionService.findAllByUser(userId);

        return ResponseEntity.ok().body(studySessions);
    }

    @PostMapping
    public ResponseEntity<StudySession> criarSessao(
            @RequestParam Integer userId,
            @RequestBody List<QuestionGenerateDTO> questoes
    ) {
        StudySession session = studySessionService
                .criarSessaoComQuestoes(userId, questoes);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(session);
    }

    @PostMapping("/generateIa")
    public ResponseEntity<StudySession> gerarSessaoComIA(
            @RequestParam Integer userId,
            @RequestBody String prompt
    ) {
        StudySession session =
                studySessionService.criarSessaoComIA(userId, prompt);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(session);
    }
}
