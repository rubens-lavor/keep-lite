package com.rubenslavor.keeplite.infrastructure.persistence.mongo

import com.rubenslavor.keeplite.domain.note.entity.Note
import com.rubenslavor.keeplite.domain.note.repository.NoteRepository
import com.rubenslavor.keeplite.infrastructure.persistence.mongo.mapper.NotePersistenceMapper
import com.rubenslavor.keeplite.infrastructure.persistence.mongo.repository.NoteMongoRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class NoteRepositoryImpl(
    private val noteMongoRepository: NoteMongoRepository
) : NoteRepository {

    override fun save(note: Note): Note {
        val noteDocument = NotePersistenceMapper.toDocument(note)
        val savedDocument = noteMongoRepository.save(noteDocument)
        return NotePersistenceMapper.toDomain(savedDocument)
    }

    override fun findByIdAndUserId(id: UUID, userId: UUID): Note? {
        val foundDocument = noteMongoRepository.findByIdAndUserId(id, userId)
        return foundDocument?.let { NotePersistenceMapper.toDomain(it) }
    }

    override fun findAllByUserId(userId: UUID): List<Note> {
        val foundDocuments = noteMongoRepository.findAllByUserId(userId)
        return foundDocuments.map { NotePersistenceMapper.toDomain(it) }
    }

    override fun deleteByIdAndUserId(id: UUID, userId: UUID) {
        noteMongoRepository.deleteByIdAndUserId(id, userId)
    }
}
