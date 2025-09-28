package com.rubenslavor.keeplite.infrastructure.persistence.mongo.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document("users")
data class UserDocument(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val password: String
)
