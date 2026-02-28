package com.ibgs.studyAssistant.question.controller;

import com.ibgs.studyAssistant.question.domain.Question;
import com.ibgs.studyAssistant.question.dto.UserAnswerDTO;
import com.ibgs.studyAssistant.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{studySessionId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Question> findAllBySession(@PathVariable UUID studySessionId){
        return questionService.findAllBySession(studySessionId);
    }
}
