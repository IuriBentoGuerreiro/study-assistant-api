package com.ibgs.studyAssistant.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@Tag(name = "HEALTH", description = "Verificação de status da aplicação")
@RestController
@RequestMapping("/health")
public class HealthController {

    @Operation(
            summary = "Verificar status da API",
            description = "Endpoint utilizado para verificar se a aplicação está online e funcionando"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aplicação funcionando corretamente")
    })
    @GetMapping
    public String healthResponse(){
        return "OK";
    }
}