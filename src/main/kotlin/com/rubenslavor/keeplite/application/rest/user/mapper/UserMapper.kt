package com.rubenslavor.keeplite.application.rest.user.mapper

import com.rubenslavor.keeplite.application.rest.user.request.RegisterRequest
import com.rubenslavor.keeplite.application.rest.user.response.UserResponse
import com.rubenslavor.keeplite.domain.user.model.RegisterModel
import com.rubenslavor.keeplite.domain.user.model.UserModel

object UserMapper {
    fun toModel(request: RegisterRequest) = RegisterModel(
        name = request.name,
        email = request.email,
        password = request.password
    )

    fun toResponse(user: UserModel) = UserResponse(
        id = user.id,
        name = user.name,
        email = user.email
    )
}
