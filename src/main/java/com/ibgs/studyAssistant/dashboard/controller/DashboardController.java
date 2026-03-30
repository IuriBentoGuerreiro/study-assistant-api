package com.ibgs.studyAssistant.dashboard.controller;

import com.ibgs.studyAssistant.dashboard.dto.DashboardDTO;
import com.ibgs.studyAssistant.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "DASHBOARD", description = "Dados consolidados para o painel do usuário")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(
            summary = "Obter dados do dashboard",
            description = "Retorna informações consolidadas para exibição no dashboard do usuário logado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados do dashboard retornados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public DashboardDTO dashboard(){
        return dashboardService.dashboard();
    }
}