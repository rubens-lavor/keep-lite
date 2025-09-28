package com.rubenslavor.keeplite.domain.auth.usecase

import com.rubenslavor.keeplite.domain.auth.model.LoginModel
import com.rubenslavor.keeplite.domain.auth.model.TokenModel

interface AuthenticateUserUseCase {
    fun execute(login: LoginModel): TokenModel
    fun refresh(token: String): TokenModel
    fun logout(refreshToken: String)
}