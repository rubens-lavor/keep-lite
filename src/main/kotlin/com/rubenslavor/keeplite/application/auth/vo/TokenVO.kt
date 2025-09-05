package com.rubenslavor.keeplite.application.auth.vo

data class TokenVO(
    val tokenType: String = "Bearer",
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String = ""
)
