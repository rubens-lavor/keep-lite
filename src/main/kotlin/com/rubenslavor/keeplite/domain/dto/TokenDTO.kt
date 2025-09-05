package com.rubenslavor.keeplite.domain.dto

data class TokenDTO(
    val tokenType: String = "Bearer",
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String = ""
)