package com.rubenslavor.keeplite.application.rest.auth.response

data class TokenResponse(
    val tokenType: String = "Bearer",
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String = ""
)
