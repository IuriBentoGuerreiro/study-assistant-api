package com.ibgs.studyAssistant.question.controller;

import com.ibgs.studyAssistant.question.domain.Question;
import com.ibgs.studyAssistant.question.dto.UserAnswerDTO;
import com.ibgs.studyAssistant.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "QUESTIONS", description = "Gerenciamento de questões e respostas do usuário")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @Operation(
            summary = "Responder uma questão",
            description = "Registra a resposta do usuário para uma questão específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resposta registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PutMapping("/user/response")
    @ResponseStatus(HttpStatus.OK)
    public void questionUserResponse(@RequestBody UserAnswerDTO userAnswerDTO){
        questionService.questionUserResponse(userAnswerDTO);
    }

    @Operation(
            summary = "Listar questões por sessão de estudo",
            description = "Retorna todas as questões associadas a uma sessão de estudo específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de questões retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/{studySessionId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Question> findAllBySession(@PathVariable UUID studySessionId){
        return questionService.findAllBySession(studySessionId);
    }
}