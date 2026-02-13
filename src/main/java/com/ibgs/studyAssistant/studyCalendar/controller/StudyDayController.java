package com.ibgs.studyAssistant.studyCalendar.controller;

import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayResponse;
import com.ibgs.studyAssistant.studyCalendar.service.StudyDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/study-day")
@RequiredArgsConstructor
public class StudyDayController {

    private final StudyDayService service;

    @PostMapping
    public ResponseEntity<StudyDayResponse> create(){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudyDayResponse> update(@PathVariable Integer id, @RequestBody StudyDayRequest request){
        return ResponseEntity.ok().body(service.update(id, request));
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<StudyDayResponse>> findByUser(@PathVariable Integer userId){
        return ResponseEntity.ok().body(service.findByUser(userId));
    }

    @GetMapping("/user/active")
    public ResponseEntity<StudyDayResponse> findActiveSession(){
        StudyDayResponse response = service.findActiveSession();

        return ResponseEntity.ok().body(response);
    };

    @PutMapping("/finish/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StudyDayResponse> finishSession(@PathVariable Integer id){
        return ResponseEntity.ok().body(service.finishSession(id));
    }
}
