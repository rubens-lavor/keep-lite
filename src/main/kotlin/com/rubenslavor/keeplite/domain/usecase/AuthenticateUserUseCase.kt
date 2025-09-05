package com.rubenslavor.keeplite.domain.usecase

import com.rubenslavor.keeplite.domain.dto.LoginDTO
import com.rubenslavor.keeplite.domain.dto.TokenDTO

interface AuthenticateUserUseCase {
    fun execute(login: LoginDTO): TokenDTO
    fun refresh(token: String): TokenDTO
}
