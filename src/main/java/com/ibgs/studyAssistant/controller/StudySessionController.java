package com.ibgs.studyAssistant.controller;

import com.ibgs.studyAssistant.domain.StudySession;
import com.ibgs.studyAssistant.dto.StudySessionNameDTO;
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

    @GetMapping("/{userId}")
    public ResponseEntity<List<StudySessionNameDTO>> findAllByUser(@PathVariable Integer userId){
        List<StudySessionNameDTO>  studySessions =  studySessionService.findAllSessionNameByUser(userId);

        return ResponseEntity.ok().body(studySessions);
    }

    @PostMapping("/generateIa")
    public ResponseEntity<StudySession> generateSession(
            @RequestParam Integer userId,
            @RequestBody String prompt,
            @RequestParam String banca,
            @RequestParam int quantidade
    ) {
        StudySession session =
                studySessionService.generateSession(userId, prompt, banca, quantidade);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(session);
    }
}
