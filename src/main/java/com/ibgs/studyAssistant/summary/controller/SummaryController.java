package com.ibgs.studyAssistant.summary.controller;

import com.ibgs.studyAssistant.summary.domain.Summary;
import com.ibgs.studyAssistant.summary.dto.SummaryTitleDTO;
import com.ibgs.studyAssistant.summary.service.SummaryService;
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

@Tag(name = "SUMMARIES", description = "Gerenciamento de resumos de estudo")
@SecurityRequirement(name = "bearerAuth") // 🔐 protegido
@RestController
@RequestMapping("/summaries")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @Operation(
            summary = "Buscar resumo por ID",
            description = "Retorna os detalhes completos de um resumo específico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumo encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Resumo não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Summary> findById(@PathVariable UUID id) {
        Summary summary = summaryService.findById(id);
        return ResponseEntity.ok(summary);
    }

    @Operation(
            summary = "Listar resumos do usuário",
            description = "Retorna uma lista resumida dos resumos criados pelo usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<SummaryTitleDTO>> findAllByUser() {
        List<SummaryTitleDTO> summary = summaryService.findAllByUser();
        return ResponseEntity.ok(summary);
    }

    @Operation(
            summary = "Gerar resumo com IA",
            description = "Gera automaticamente um resumo de estudo com base em um texto/prompt fornecido pelo usuário. Pode levar alguns segundos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resumo gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Prompt inválido"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping("/generate")
    public ResponseEntity<Summary> generateSummary(@RequestBody String prompt) {
        Summary summary = summaryService.generateSumarry(prompt);
        return ResponseEntity.status(HttpStatus.CREATED).body(summary);
    }

    @Operation(
            summary = "Excluir resumo",
            description = "Remove um resumo pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resumo removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Resumo não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        summaryService.delete(id);
    }
}