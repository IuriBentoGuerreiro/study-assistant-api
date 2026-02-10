package com.ibgs.studyAssistant.studyCalendar.controller;

import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayResponse;
import com.ibgs.studyAssistant.studyCalendar.service.StudyDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/study-day")
@RequiredArgsConstructor
public class StudyDayController {

    private final StudyDayService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudyDayResponse create(@RequestBody StudyDayRequest request){
        return service.create(request);
    }
}
