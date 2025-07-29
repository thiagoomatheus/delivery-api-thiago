package com.deliverytech.delivery_api.controller;
 
import java.time.LocalDateTime;
import java.util.Map;
 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Health", description = "Endpoints para verificar a saúde do serviço")
@RestController
public class HealthController {

    @GetMapping("/health")
    @Operation(
        summary = "Verificar a saúde do serviço",
        description = "Retorna o status de saúde do serviço, incluindo timestamp e informações do ambiente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Serviço está ativo e saudável"),
        @ApiResponse(responseCode = "503", description = "Serviço está indisponível")
    })
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now().toString(),
            "service", "Delivery API",
            "javaVersion", System.getProperty("java.version")
        );
    }

    @GetMapping("/info")
    @Operation(
        summary = "Informações sobre o serviço",
        description = "Retorna informações sobre o serviço, incluindo versão, desenvolvedor, JDK e framework."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Informações sobre o serviço retornadas com sucesso")
    })
    public AppInfo info() {
        return new AppInfo(
            "Delivery Tech API",
            "1.0.0",
            "Thiago Oliveira Matheus",
            "JDK 21",
            "Spring Boot 3.2.x"
        );
    }

    // Record para demonstrar recurso do Java 14+ (disponível no JDK 21)
    public record AppInfo(
        String application,
        String version,
        String developer,
        String javaVersion,
        String framework
    ) {}
}
