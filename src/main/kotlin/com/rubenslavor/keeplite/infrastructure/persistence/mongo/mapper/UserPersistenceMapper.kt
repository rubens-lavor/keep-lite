package com.rubenslavor.keeplite.infrastructure.persistence.mongo.mapper

import com.rubenslavor.keeplite.domain.user.entity.User
import com.rubenslavor.keeplite.infrastructure.persistence.mongo.document.UserDocument

object UserPersistenceMapper {
    fun toDocument(user: User): UserDocument =
        UserDocument(
            name = user.name,
            email = user.email,
            password = user.password
        )

    fun toModel(document: UserDocument): User =
        User(
            id = document.id,
            name = document.name,
            email = document.email,
            password = document.password
        )
}
