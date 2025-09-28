package com.rubenslavor.keeplite.infrastructure.config

import com.rubenslavor.keeplite.infrastructure.security.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider
) {
    companion object {
        // Lista centralizada de todas as rotas públicas
        private val PUBLIC_ROUTES = arrayOf(
            // Rotas de Autenticação
            "/api/v1/auth/**",
            "/api/v1/user/register",

            // Rotas do Spring Actuator
            "/actuator/**",

            // Rotas do Swagger/OpenAPI
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
        )
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // Desabilita CSRF, comum em APIs stateless
            .authorizeHttpRequests {
                it
                    // Permite acesso público a todas as rotas na lista
                    //* (spread operator do Kotlin) "desempacota" os itens da lista PUBLIC_ROUTES para dentro da função.
                    .requestMatchers(*PUBLIC_ROUTES).permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Define a política de sessão como stateless
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java) // Adiciona nosso filtro JWT

        return http.build()
    }
}