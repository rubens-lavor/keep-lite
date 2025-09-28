package com.rubenslavor.keeplite.application.rest.auth.controller.v1

import com.rubenslavor.keeplite.application.rest.auth.mapper.AuthMapper
import com.rubenslavor.keeplite.application.rest.auth.request.LoginRequest
import com.rubenslavor.keeplite.application.rest.auth.response.TokenResponse
import com.rubenslavor.keeplite.domain.auth.usecase.AuthenticateUserUseCase
import jakarta.validation.Valid
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

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticateUser: AuthenticateUserUseCase
) {
    @PostMapping("/login", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<TokenResponse> {

        val login = AuthMapper.toModel(request)
        val token = authenticateUser.execute(login)

        // TODO: [DÚVIDA] aqui vai o tempo do accessToken ou do refreshToken??
        val refreshCookie = cookie(token.refreshToken, maxAge = 15)
        val body: TokenResponse = AuthMapper.toResponse(token)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .body(body)
    }

    @PostMapping("/refresh", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun refresh(@CookieValue("refresh_token") refreshToken: String): ResponseEntity<TokenResponse> {
        val token = authenticateUser.refresh(refreshToken)

        val newRefreshCookie = cookie(refreshToken, maxAge = 15) // TODO: [DÚVIDA] aqui vai o tempo do accessToken ou do refreshToken??
        val body: TokenResponse = AuthMapper.toResponse(token)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .body(body)
    }

    @PostMapping("/logout")
    fun logout(@CookieValue("refresh_token") refreshToken: String): ResponseEntity<Void> {
        authenticateUser.logout(refreshToken)

        val expiredCookie = cookie(maxAge = 0)

        return ResponseEntity.noContent()
            .header(HttpHeaders.SET_COOKIE, expiredCookie.toString()) // TODO: [DÚVIDA] pq estou mando isso no header?
            .build()
    }

    // TODO: [DÚVIDA] preciso entender a nessecidade desse método tbm
    private fun cookie(token: String = "", maxAge: Long) = ResponseCookie
        .from("refresh_token", token)
        .httpOnly(true)
        .secure(true)
        .path("/api/v1/auth/refresh")
        .sameSite("Strict")
        .maxAge(Duration.ofMinutes(maxAge))
        .build()
}