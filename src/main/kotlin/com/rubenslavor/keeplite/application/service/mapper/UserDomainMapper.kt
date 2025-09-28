package com.rubenslavor.keeplite.application.service.mapper

import com.rubenslavor.keeplite.domain.user.entity.User
import com.rubenslavor.keeplite.domain.user.model.UserModel

object UserDomainMapper {
    fun toModel(user: User) = UserModel(
        id = user.id,
        name = user.name,
        email = user.email
    )
}
