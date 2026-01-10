package com.ibgs.studyAssistant.controller;

import com.ibgs.studyAssistant.dto.DashboardDTO;
import com.ibgs.studyAssistant.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public DashboardDTO dashboard(){
        return dashboardService.dashboard();
    }
}
