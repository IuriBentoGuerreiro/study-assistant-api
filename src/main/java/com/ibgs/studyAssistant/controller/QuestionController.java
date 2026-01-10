package com.ibgs.studyAssistant.controller;

import com.ibgs.studyAssistant.dto.UserAnswerDTO;
import com.ibgs.studyAssistant.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PutMapping("/user/response")
    @ResponseStatus(HttpStatus.OK)
    public void questionUserResponse(@RequestBody UserAnswerDTO userAnswerDTO){
        questionService.questionUserResponse(userAnswerDTO);
    }
}
