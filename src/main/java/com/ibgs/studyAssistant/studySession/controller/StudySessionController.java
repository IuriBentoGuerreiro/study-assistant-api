package com.ibgs.studyAssistant.studySession.controller;

import com.ibgs.studyAssistant.studySession.dto.PromptRequest;
import com.ibgs.studyAssistant.studySession.dto.StudySessionNameDTO;
import com.ibgs.studyAssistant.studySession.dto.StudySessionResponseDTO;
import com.ibgs.studyAssistant.studySession.service.StudySessionService;
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

    @GetMapping
    public ResponseEntity<List<StudySessionNameDTO>> findAllByUser() {
        List<StudySessionNameDTO> studySessions = studySessionService.findAllSessionNameByUser();

        return ResponseEntity.ok().body(studySessions);
    }

    @GetMapping("/{id}/full")
    public ResponseEntity<StudySessionResponseDTO> getFullSession(@PathVariable Integer id) {
        return ResponseEntity.ok(studySessionService.findFullSession(id));
    }

    @PostMapping("/generateIa")
    public ResponseEntity<StudySessionResponseDTO> generate(@RequestBody PromptRequest request) {
        StudySessionResponseDTO response = studySessionService.generateSession(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        studySessionService.delete(id);
    }
}
