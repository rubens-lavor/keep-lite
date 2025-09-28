package com.rubenslavor.keeplite.domain.user.repository

import com.rubenslavor.keeplite.domain.user.entity.User
import java.util.UUID

interface UserRepository {
    fun save(user: User): User
    fun findByEmail(email: String): User?
    fun findById(id: UUID): User?
}