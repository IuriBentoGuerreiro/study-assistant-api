package com.ibgs.studyAssistant.studySession.controller;

import com.ibgs.studyAssistant.studySession.dto.PromptRequest;
import com.ibgs.studyAssistant.studySession.dto.StudySessionNameDTO;
import com.ibgs.studyAssistant.studySession.dto.StudySessionResponseDTO;
import com.ibgs.studyAssistant.studySession.service.StudySessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "STUDY SESSIONS", description = "Gerenciamento de sessões de estudo")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class StudySessionController {

    private final StudySessionService studySessionService;

    @Operation(
            summary = "Listar sessões do usuário",
            description = "Retorna uma lista resumida das sessões de estudo do usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessões retornadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<StudySessionNameDTO>> findAllByUser() {
        List<StudySessionNameDTO> studySessions = studySessionService.findAllSessionNameByUser();
        return ResponseEntity.ok().body(studySessions);
    }

    @Operation(
            summary = "Buscar sessão completa",
            description = "Retorna todos os detalhes de uma sessão de estudo específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/{id}/full")
    public ResponseEntity<StudySessionResponseDTO> getFullSession(@PathVariable UUID id) {
        return ResponseEntity.ok(studySessionService.findFullSession(id));
    }

    @Operation(
            summary = "Gerar sessão de estudo com IA",
            description = "Gera automaticamente uma sessão de estudo com base em um prompt fornecido pelo usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão gerada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Prompt inválido"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping("/generateIa")
    public ResponseEntity<StudySessionResponseDTO> generate(@RequestBody PromptRequest request) {
        StudySessionResponseDTO response = studySessionService.generateSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Excluir sessão de estudo",
            description = "Remove uma sessão de estudo pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sessão removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        studySessionService.delete(id);
    }
}