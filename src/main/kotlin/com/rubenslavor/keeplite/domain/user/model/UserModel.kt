package com.rubenslavor.keeplite.domain.user.model

import java.util.UUID

data class UserModel(
    val id: UUID,
    val name: String,
    val email: String
)
