package com.rubenslavor.keeplite.infrastructure.persistence.mongo.repository

import com.rubenslavor.keeplite.infrastructure.persistence.mongo.document.NoteDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface NoteMongoRepository : MongoRepository<NoteDocument, UUID> {
    fun findByIdAndUserId(id: UUID, userId: UUID): NoteDocument?
    fun findAllByUserId(userId: UUID): List<NoteDocument>
    fun deleteByIdAndUserId(id: UUID, userId: UUID)
}