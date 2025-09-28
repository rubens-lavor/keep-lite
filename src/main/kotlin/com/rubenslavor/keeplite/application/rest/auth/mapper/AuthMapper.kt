package com.rubenslavor.keeplite.application.rest.auth.mapper

import com.rubenslavor.keeplite.application.rest.auth.request.LoginRequest
import com.rubenslavor.keeplite.application.rest.auth.response.TokenResponse
import com.rubenslavor.keeplite.domain.auth.model.LoginModel
import com.rubenslavor.keeplite.domain.auth.model.TokenModel

object AuthMapper {

    fun toModel(request: LoginRequest) = LoginModel(
        email = request.email,
        password = request.password
    )

    fun toResponse(token: TokenModel) = TokenResponse(
        tokenType = token.tokenType,
        accessToken = token.accessToken,
        expiresIn = token.expiresIn,
        refreshToken = token.refreshToken
    )
}
