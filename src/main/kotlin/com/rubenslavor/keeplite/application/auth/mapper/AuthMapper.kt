package com.rubenslavor.keeplite.application.auth.mapper

import com.rubenslavor.keeplite.application.auth.request.LoginRequest
import com.rubenslavor.keeplite.application.auth.request.RegisterRequest
import com.rubenslavor.keeplite.application.auth.vo.TokenVO
import com.rubenslavor.keeplite.application.auth.vo.UserVO
import com.rubenslavor.keeplite.domain.dto.LoginDTO
import com.rubenslavor.keeplite.domain.dto.RegisterDTO
import com.rubenslavor.keeplite.domain.dto.TokenDTO
import com.rubenslavor.keeplite.domain.dto.UserDTO

object AuthMapper {
    fun toDTO(request: RegisterRequest) = RegisterDTO(
        name = request.name,
        email = request.email,
        password = request.password
    )

    fun toDTO(request: LoginRequest) = LoginDTO(
        email = request.email,
        password = request.password
    )

    fun toVO(user: UserDTO) = UserVO(
        id = user.id,
        name = user.name,
        email = user.email
    )

    fun toVO(token: TokenDTO) = TokenVO(
        tokenType = token.tokenType,
        accessToken = token.accessToken,
        expiresIn = token.expiresIn,
        refreshToken = token.refreshToken
    )
}
