package com.ibgs.studyAssistant.studyCalendar.controller;

import com.ibgs.studyAssistant.studyCalendar.dto.studyGoal.StudyGoalRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyGoal.StudyGoalResponse;
import com.ibgs.studyAssistant.studyCalendar.service.StudyGoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "STUDY GOALS", description = "Gerenciamento de metas de estudo do usuário")
@SecurityRequirement(name = "bearerAuth") // 🔐 protegido
@RestController
@RequestMapping("/study-goal")
@RequiredArgsConstructor
public class StudyGoalController {

    private final StudyGoalService service;

    @Operation(
            summary = "Criar meta de estudo",
            description = "Cria uma nova meta de estudo para o usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Meta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping
    public ResponseEntity<StudyGoalResponse> create(
            @Valid @RequestBody StudyGoalRequest request
    ) {
        StudyGoalResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Atualizar meta de estudo",
            description = "Atualiza uma meta de estudo existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Meta atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Meta não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudyGoalResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody StudyGoalRequest request
    ) {
        StudyGoalResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar meta do usuário",
            description = "Retorna a meta de estudo atual do usuário logado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Meta encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Meta não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StudyGoalResponse> findByUser(){
        StudyGoalResponse response = service.findByUser();
        return ResponseEntity.ok(response);
    }
}