package com.ibgs.studyAssistant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartController {

    @GetMapping("/heart")
    public String health() {
        return "OK";
    }
}
