package com.rubenslavor.keeplite.application.rest.user.response

import java.util.UUID

data class UserResponse(
    val id: UUID,
    val name: String,
    val email: String
)
