package com.rubenslavor.keeplite.application.service

import com.rubenslavor.keeplite.infrastructure.security.jwt.JwtProvider
import com.rubenslavor.keeplite.domain.auth.model.LoginModel
import com.rubenslavor.keeplite.domain.auth.model.TokenModel
import com.rubenslavor.keeplite.domain.exceptions.BusinessException
import com.rubenslavor.keeplite.domain.auth.usecase.AuthenticateUserUseCase
import com.rubenslavor.keeplite.domain.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.Base64
import java.util.UUID
import java.util.concurrent.TimeUnit

@Component
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwt: JwtProvider,
    private val redis: StringRedisTemplate
) : AuthenticateUserUseCase {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun execute(login: LoginModel): TokenModel {
        log.info("Iniciando tentativa de login para o e-mail: {}", login.email)

        val user = userRepository.findByEmail(login.email)
            ?: run {
                log.warn("Falha no login: e-mail não encontrado: {}", login.email)
                throw BusinessException("Invalid credentials")
            }

        if (!passwordEncoder.matches(login.password, user.password)) {
            log.warn("Falha no login: senha incorreta para o e-mail: {}", login.email)
            throw BusinessException("Invalid credentials")
        }

        log.info("Credenciais válidas para o usuário ID: {}. Gerando tokens...", user.id)

        val accessToken = jwt.generateAccessToken(user)
        val refreshToken = jwt.generateRefreshToken(user)

        return TokenModel(
            tokenType = "Bearer",
            accessToken = accessToken,
            expiresIn = jwt.getExpiration(accessToken).time,
            refreshToken = refreshToken
        ).also {
            log.info("Tokens gerados com sucesso para o usuário ID: {}", user.id)
        }
    }

    override fun refresh(token: String): TokenModel {
        log.info("Recebida requisição para renovar token.")

        val hash = hashToken(token)
        if (redis.hasKey("revoked:refresh:$hash")) {
            log.warn("Tentativa de uso de refresh token revogado. Hash: {}", hash)
            throw BusinessException("Token inválido")
        }

        val userId = jwt.validateRefreshToken(token).subject
        val user = userRepository.findById(UUID.fromString(userId))
            ?: run {
                log.error("Usuário não encontrado para o ID de usuário no token de refresh: {}", userId)
                throw BusinessException("Usuário não encontrado")
            }

        log.info("Refresh token validado para o usuário ID: {}. Gerando novos tokens.", userId)

        val newAccessToken = jwt.generateAccessToken(user)
        val newRefreshToken = jwt.generateRefreshToken(user)

        return TokenModel(
            tokenType = "Bearer",
            accessToken = newAccessToken,
            expiresIn = jwt.getExpiration(newRefreshToken).time,
            refreshToken = newRefreshToken
        ).also {
            log.info("Novos tokens gerados com sucesso para o usuário ID: {}", userId)
        }
    }

    override fun logout(refreshToken: String) {
        log.info("Recebida requisição de logout.")

        val claims = jwt.validateRefreshToken(refreshToken)

        val exp = claims.expiration
        val ttl = (exp.time - System.currentTimeMillis()).coerceAtLeast(0)
        val userId = claims.subject
        val hash = hashToken(refreshToken)

        redis.opsForValue().set(
            "revoked:refresh:$hash",
            "revoked by userId:$userId",
            ttl,
            TimeUnit.MILLISECONDS
        ).also {
            log.info("Refresh token para o usuário ID: {} foi revogado com sucesso. TTL no Redis: {} ms.", userId, ttl)
        }
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(token.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash)
    }
}
