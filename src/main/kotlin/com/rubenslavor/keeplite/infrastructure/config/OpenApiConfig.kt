package com.rubenslavor.keeplite.infrastructure.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenApi(): OpenAPI {
        val securitySchemeName = "bearerAuth"

        return OpenAPI()
            .info(
                Info()
                    .title("Keep Lite API")
                    .version("1.0.0")
                    .description("API para gerenciamento de notas, projeto do TCC.")
            )
            // Aplica o esquema de segurança globalmente a todos os endpoints
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            // Define o esquema de segurança
            .components(
                Components()
                    .addSecuritySchemes(
                        securitySchemeName,
                        SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP) // Tipo de segurança: HTTP
                            .scheme("bearer")               // Esquema específico: Bearer
                            .bearerFormat("JWT")            // Formato do token: JWT
                    )
            )
    }
}