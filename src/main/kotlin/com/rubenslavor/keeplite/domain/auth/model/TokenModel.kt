package com.rubenslavor.keeplite.domain.auth.model

data class TokenModel(
    val tokenType: String = "Bearer",
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String = ""
)