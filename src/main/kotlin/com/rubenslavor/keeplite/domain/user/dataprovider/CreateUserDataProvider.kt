package com.rubenslavor.keeplite.domain.user.dataprovider

import com.rubenslavor.keeplite.domain.user.model.UserModel
import com.rubenslavor.keeplite.domain.user.entity.User

interface CreateUserDataProvider {
    fun create(user: User): UserModel
}