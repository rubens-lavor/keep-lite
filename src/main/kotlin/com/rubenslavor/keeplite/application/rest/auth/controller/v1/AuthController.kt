package com.rubenslavor.keeplite.application.rest.auth.controller.v1

import com.rubenslavor.keeplite.application.rest.auth.mapper.AuthMapper
import com.rubenslavor.keeplite.application.rest.auth.request.LoginRequest
import com.rubenslavor.keeplite.application.rest.auth.request.LogoutRequest
import com.rubenslavor.keeplite.application.rest.auth.response.TokenResponse
import com.rubenslavor.keeplite.domain.auth.usecase.AuthenticateUserUseCase
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@Tag(name = "Auth Controller", description = "Controller de autenticação")
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticateUser: AuthenticateUserUseCase,
    @param:Value("\${jwt.refresh-token-expiration-ms}")
    private val refreshTokenDurationMs: Long
) {
    @PostMapping("/login", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<TokenResponse> {

        val login = AuthMapper.toModel(request)
        val token = authenticateUser.execute(login)

        val refreshCookie = cookie(token.refreshToken, maxAge = refreshTokenDurationMs / 60000)
        val body: TokenResponse = AuthMapper.toResponse(token)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .body(body)
    }

    @PostMapping("/refresh", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun refresh(@CookieValue("refresh_token") refreshToken: String): ResponseEntity<TokenResponse> {
        val token = authenticateUser.refresh(refreshToken)

        val refreshCookie = cookie(token.refreshToken, maxAge = refreshTokenDurationMs / 60000)
        val body: TokenResponse = AuthMapper.toResponse(token)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .body(body)
    }

    @PostMapping("/logout")
    fun logout(@RequestBody request: LogoutRequest): ResponseEntity<Void> {
        authenticateUser.logout(request.refreshToken)

        val expiredCookie = cookie(maxAge = 0)

        return ResponseEntity.noContent()
            .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
            .build()
    }

    private fun cookie(token: String = "", maxAge: Long) = ResponseCookie
        .from("refresh_token", token)
        .httpOnly(true)
        .secure(true)
        .path("/api/v1/auth/refresh")
        .sameSite("Strict")
        .maxAge(Duration.ofMinutes(maxAge))
        .build()
}