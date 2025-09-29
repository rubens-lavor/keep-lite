package com.rubenslavor.keeplite.infrastructure.security.jwt

import com.rubenslavor.keeplite.domain.user.model.UserModel
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import com.rubenslavor.keeplite.domain.user.entity.User
import io.jsonwebtoken.Claims
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtProvider(
    @param:Value("\${jwt.secret}")
    private val secret: String,
    @param:Value("\${jwt.access-token-expiration-ms}")
    private val accessTokenExpiration: Long,
    @param:Value("\${jwt.refresh-token-expiration-ms}")
    private val refreshTokenExpiration: Long
) {

    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    fun generateAccessToken(user: User): String {
        return generateToken(user.email, accessTokenExpiration)
    }

    fun generateRefreshToken(user: User): String {
        return generateToken(user.email, refreshTokenExpiration)
    }

    private fun generateToken(subject: String, expiration: Long): String {
        val now = Date()
        val expirationDate = Date(now.time + expiration)
        return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expirationDate)
            .signWith(secretKey)
            .compact()
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            !isTokenExpired(token)
        } catch (e: Exception) {
            false
        }
    }

    fun getEmailFromToken(token: String): String {
        return getAllClaimsFromToken(token).subject
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getAllClaimsFromToken(token).expiration
        return expiration.before(Date())
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun validateRefreshToken(token: String): String {
        val claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body

        if (claims["type"] != "refresh") throw IllegalArgumentException("Invalid token type")
        return claims.subject // userId
    }

    fun getExpiration(token: String): Date {
        val claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body

        return claims.expiration
    }
}