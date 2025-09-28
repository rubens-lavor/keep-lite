package com.rubenslavor.keeplite.domain.user.usecase

import com.rubenslavor.keeplite.domain.user.model.RegisterModel
import com.rubenslavor.keeplite.domain.user.model.UserModel

interface RegisterUserUseCase {
    fun execute(register: RegisterModel): UserModel
}
