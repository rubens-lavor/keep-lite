package com.rubenslavor.keeplite.domain.user.entity

import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val password: String,
    val roles: Set<String> = setOf("USER")
)