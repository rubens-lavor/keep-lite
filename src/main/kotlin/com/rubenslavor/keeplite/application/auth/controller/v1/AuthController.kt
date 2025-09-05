package com.rubenslavor.keeplite.application.auth.controller.v1

import com.rubenslavor.keeplite.application.auth.mapper.AuthMapper
import com.rubenslavor.keeplite.application.auth.request.LoginRequest
import com.rubenslavor.keeplite.application.auth.request.RegisterRequest
import com.rubenslavor.keeplite.application.auth.vo.TokenVO
import com.rubenslavor.keeplite.application.auth.vo.UserVO
import com.rubenslavor.keeplite.domain.dto.TokenDTO
import com.rubenslavor.keeplite.domain.dto.UserDTO
import com.rubenslavor.keeplite.domain.usecase.AuthenticateUserUseCase
import com.rubenslavor.keeplite.domain.usecase.RegisterUserUseCase
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.time.Duration

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val registerUser: RegisterUserUseCase,
    private val authenticateUser: AuthenticateUserUseCase,
) {

    @PostMapping("/register", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<UserVO> {
        val createdUser = registerUser.execute(register = request.toDTO())

        // 2) monta Location do recurso criado (quando houver /api/v1/users/{id})
        val location = uRI(createdUser)
        val body: UserVO = AuthMapper.toVO(user = createdUser)

        return ResponseEntity.created(location).body(body)
    }

    @PostMapping("/login", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<TokenVO> {
        val login = AuthMapper.toDTO(request)
        val token = authenticateUser.execute(login)

        val refreshCookie = cookie(token)
        val body: TokenVO = AuthMapper.toVO(token)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .body(body)
    }

    @PostMapping("/refresh", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun refresh(@CookieValue("refresh_token") refreshToken: String): ResponseEntity<TokenVO> {
        val token = authenticateUser.refresh(refreshToken)

        val newRefreshCookie = cookie(token)
        val body: TokenVO = AuthMapper.toVO(token)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .body(body)
    }

    private fun cookie(token: TokenDTO): ResponseCookie = ResponseCookie.from("refresh_token", token.refreshToken)
        .httpOnly(true)
        .secure(true)
        .path("/api/v1/auth/refresh")
        .sameSite("Strict")
        .maxAge(Duration.ofMinutes(30))
        .build()

    private fun uRI(createdUser: UserDTO): URI = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("/api/v1/users/{id}")
        .buildAndExpand(createdUser.id)
        .toUri()
}