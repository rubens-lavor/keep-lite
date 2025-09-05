package com.rubenslavor.keeplite.application.auth.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank
    val name: String,
    @field:Email
    val email: String,
    @field:Size(min = 8, max = 72)
    val password: String
)
