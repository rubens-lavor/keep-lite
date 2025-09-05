package com.rubenslavor.keeplite.domain.usecase

import com.rubenslavor.keeplite.domain.dto.RegisterDTO
import com.rubenslavor.keeplite.domain.dto.UserDTO

interface RegisterUserUseCase {
    fun execute(register: RegisterDTO): UserDTO
}
