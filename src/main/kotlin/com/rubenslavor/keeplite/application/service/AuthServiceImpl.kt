package com.rubenslavor.keeplite.application.service

import com.rubenslavor.keeplite.application.service.mapper.UserDomainMapper
import com.rubenslavor.keeplite.infrastructure.security.jwt.JwtProvider
import com.rubenslavor.keeplite.domain.auth.model.LoginModel
import com.rubenslavor.keeplite.domain.auth.model.TokenModel
import com.rubenslavor.keeplite.domain.exceptions.BusinessException
import com.rubenslavor.keeplite.domain.auth.usecase.AuthenticateUserUseCase
import com.rubenslavor.keeplite.domain.user.repository.UserRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import java.security.MessageDigest
import java.util.Base64
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.TimeUnit

@Component
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwt: JwtProvider,
    private val redis: StringRedisTemplate
) : AuthenticateUserUseCase {

    // TODO: Colocar log info nos use cases

    override fun execute(login: LoginModel): TokenModel {
        val user = userRepository.findByEmail(login.email)
            ?: throw BusinessException("Invalid credentials")

        if (!passwordEncoder.matches(login.password, user.password)) {
            throw BusinessException("Invalid credentials")
        }

        val userModel = UserDomainMapper.toModel(user)
        val accessToken = jwt.generateAccessToken(userModel)
        val refreshToken = jwt.generateRefreshToken(userModel)

        return TokenModel(
            tokenType = "Bearer",
            accessToken = accessToken,
            expiresIn = jwt.getExpiration(accessToken).time,
            refreshToken = refreshToken
        )
    }

    override fun refresh(token: String): TokenModel {
        val hash = hashToken(token)
        if (redis.hasKey("revoked:refresh:$hash")) {
            throw BusinessException("Token inválido")
        }

        val userId = jwt.validateRefreshToken(token)
        val user = userRepository.findById(UUID.fromString(userId))
            ?: throw BusinessException("Usuário não encontrado")

        val userModel = UserDomainMapper.toModel(user)
        val accessToken = jwt.generateAccessToken(userModel)
        val refreshToken = jwt.generateRefreshToken(userModel)

        return TokenModel(
            tokenType = "Bearer",
            accessToken = accessToken,
            expiresIn = jwt.getExpiration(token).time,
            refreshToken = refreshToken
        )
    }

    override fun logout(refreshToken: String) {
        val exp = jwt.getExpiration(refreshToken)
        val ttl = (exp.time - System.currentTimeMillis()).coerceAtLeast(0)
        val userId = jwt.validateRefreshToken(refreshToken)
        val hash = hashToken(refreshToken)

        redis.opsForValue().set(
            "revoked:refresh:$hash",
            "revoked by userId:$userId",
            ttl,
            TimeUnit.MILLISECONDS
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(token.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash)
    }

}
