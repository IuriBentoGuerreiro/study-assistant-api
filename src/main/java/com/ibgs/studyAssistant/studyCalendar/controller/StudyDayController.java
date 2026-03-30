package com.ibgs.studyAssistant.studyCalendar.controller;

import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.*;
import com.ibgs.studyAssistant.studyCalendar.service.StudyDayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "STUDY DAYS", description = "Gerenciamento dos dias de estudo do usuário")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/study-day")
@RequiredArgsConstructor
public class StudyDayController {

    private final StudyDayService service;

    @Operation(
            summary = "Criar dia de estudo automaticamente",
            description = "Cria um dia de estudo baseado em uma descrição (ex: planejamento automático)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dia de estudo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping
    public ResponseEntity<StudyDayResponse> create(@RequestBody StudyDayDescriptionRequest studyDayDescription){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(studyDayDescription));
    }

    @Operation(
            summary = "Criar dia de estudo manualmente",
            description = "Permite criar um dia de estudo informando os dados manualmente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dia de estudo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping("/manual")
    public ResponseEntity<StudyDayResponse> createManual(@RequestBody StudyDayManualRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createManual(request));
    }

    @Operation(
            summary = "Atualizar dia de estudo",
            description = "Atualiza as informações de um dia de estudo existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dia atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Dia de estudo não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudyDayResponse> update(@PathVariable UUID id, @RequestBody StudyDayRequest request){
        return ResponseEntity.ok().body(service.update(id, request));
    }

    @Operation(
            summary = "Buscar calendário de estudos",
            description = "Retorna os dias de estudo do usuário dentro de um intervalo de datas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/calendar")
    public ResponseEntity<List<StudyDayResponse>> findByUser(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end
    ){
        return ResponseEntity.ok().body(service.findByUser(start, end));
    }

    @Operation(
            summary = "Remover dia de estudo",
            description = "Exclui um dia de estudo pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dia removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Dia não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        service.deleteById(id);
    }

    @Operation(
            summary = "Buscar sessão ativa",
            description = "Retorna o dia de estudo atualmente em andamento"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão ativa encontrada"),
            @ApiResponse(responseCode = "404", description = "Nenhuma sessão ativa"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/user/active")
    public ResponseEntity<StudyDayResponse> findActiveSession(){
        StudyDayResponse response = service.findActiveSession();
        return ResponseEntity.ok().body(response);
    }

    @Operation(
            summary = "Finalizar sessão de estudo",
            description = "Finaliza um dia de estudo em andamento"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão finalizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PutMapping("/finish/{id}")
    public ResponseEntity<StudyDayResponse> finishSession(@PathVariable UUID id){
        return ResponseEntity.ok().body(service.finishSession(id));
    }
}