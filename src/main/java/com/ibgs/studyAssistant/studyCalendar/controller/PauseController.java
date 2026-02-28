package com.ibgs.studyAssistant.studyCalendar.controller;

import com.ibgs.studyAssistant.studyCalendar.dto.pause.PauseResponse;
import com.ibgs.studyAssistant.studyCalendar.service.PauseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pauses")
@RequiredArgsConstructor
public class PauseController {

    private final PauseService service;

    @PostMapping("/study-day/{studyDayId}")
    public ResponseEntity<PauseResponse> initPause(@PathVariable UUID studyDayId) {
        PauseResponse response = service.initPause(studyDayId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<Void> finishPause(@PathVariable UUID id) {
        service.finishPause(id);
        return ResponseEntity.noContent().build();
    }
}