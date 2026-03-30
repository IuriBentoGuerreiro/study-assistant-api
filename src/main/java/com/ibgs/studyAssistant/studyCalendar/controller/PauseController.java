package com.ibgs.studyAssistant.studyCalendar.controller;

import com.ibgs.studyAssistant.studyCalendar.dto.pause.PauseResponse;
import com.ibgs.studyAssistant.studyCalendar.service.PauseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "PAUSES", description = "Gerenciamento de pausas durante sessões de estudo")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/pauses")
@RequiredArgsConstructor
public class PauseController {

    private final PauseService service;

    @Operation(
            summary = "Iniciar uma pausa",
            description = "Cria uma nova pausa associada a um dia de estudo"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pausa iniciada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Dia de estudo não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping("/study-day/{studyDayId}")
    public ResponseEntity<PauseResponse> initPause(@PathVariable UUID studyDayId) {
        PauseResponse response = service.initPause(studyDayId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Finalizar uma pausa",
            description = "Finaliza uma pausa existente informando seu horário de término"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pausa finalizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pausa não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PatchMapping("/{id}/finish")
    public ResponseEntity<Void> finishPause(@PathVariable UUID id) {
        service.finishPause(id);
        return ResponseEntity.noContent().build();
    }
}