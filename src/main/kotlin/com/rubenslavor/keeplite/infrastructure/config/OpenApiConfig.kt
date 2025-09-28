package com.rubenslavor.keeplite.infrastructure.config

import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Keep Lite API")
                    .version("1.0.0")
                    .description("API para gerenciamento de notas, projeto do TCC.")
            )
    }
}