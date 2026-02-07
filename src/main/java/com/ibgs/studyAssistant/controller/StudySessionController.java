package com.ibgs.studyAssistant.controller;

import com.ibgs.studyAssistant.dto.PromptRequest;
import com.ibgs.studyAssistant.dto.StudySessionNameDTO;
import com.ibgs.studyAssistant.dto.StudySessionResponseDTO;
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
    public ResponseEntity<List<StudySessionNameDTO>> findAllByUser(@PathVariable Integer userId) {
        List<StudySessionNameDTO> studySessions = studySessionService.findAllSessionNameByUser(userId);

        return ResponseEntity.ok().body(studySessions);
    }

    @PostMapping("/generateIa")
    public StudySessionResponseDTO generate(
            @RequestBody PromptRequest request,
            @RequestParam Integer userId
    ) {
        return studySessionService.generateSession(
                request, userId
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        studySessionService.delete(id);
    }
}
