package com.rubenslavor.keeplite.domain.user.dataprovider

import com.rubenslavor.keeplite.domain.user.model.UserModel

interface FetchUserDataProvider {
    fun fetchByEmail(email: String): UserModel?
    fun fetchByUserId(userId: String): UserModel?
}