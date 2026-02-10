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
    public ResponseEntity<StudyGoalResponse> createOrUpdate(
            @Valid @RequestBody StudyGoalRequest request
    ) {
        StudyGoalResponse response = service.createOrUpdate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public StudyGoalResponse findByUser(@PathVariable Integer userId){
        return service.findByUser(userId);
    }
}
