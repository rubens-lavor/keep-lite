package com.rubenslavor.keeplite.infrastructure.persistence.mongo.repository

import com.rubenslavor.keeplite.infrastructure.persistence.mongo.document.UserDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserMongoRepository : MongoRepository<UserDocument, UUID> {
    fun findByEmail(email: String): UserDocument?
}