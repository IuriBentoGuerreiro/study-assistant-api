package com.ibgs.studyAssistant.studyCalendar.controller;

import com.ibgs.studyAssistant.studyCalendar.dto.studyGoal.StudyGoalRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyGoal.StudyGoalResponse;
import com.ibgs.studyAssistant.studyCalendar.service.StudyGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/study-goal")
@RequiredArgsConstructor
public class StudyGoalController {

    private final StudyGoalService service;

    @PostMapping
    public ResponseEntity<StudyGoalResponse> create (
            @Valid @RequestBody StudyGoalRequest request
    ) {
        StudyGoalResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudyGoalResponse> update(
            @PathVariable Integer id, @Valid @RequestBody StudyGoalRequest request
    ) {
        StudyGoalResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StudyGoalResponse> findByUser(){
        StudyGoalResponse response = service.findByUser();

        return ResponseEntity.ok(response);
    }
}
