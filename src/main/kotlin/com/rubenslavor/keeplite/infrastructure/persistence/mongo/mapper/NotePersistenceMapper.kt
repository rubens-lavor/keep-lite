package com.rubenslavor.keeplite.infrastructure.persistence.mongo.mapper

import com.rubenslavor.keeplite.domain.note.entity.ChecklistItem
import com.rubenslavor.keeplite.domain.note.entity.Note
import com.rubenslavor.keeplite.infrastructure.persistence.mongo.document.NoteDocument.ChecklistItemDocument
import com.rubenslavor.keeplite.infrastructure.persistence.mongo.document.NoteDocument

object NotePersistenceMapper {

    fun toDomain(document: NoteDocument): Note {
        return Note(
            id = document.id,
            userId = document.userId,
            title = document.title,
            content = document.content,
            tags = document.tags,
            color = document.color,
            pinned = document.pinned,
            archived = document.archived,
            checklist = document.checklist.map { ChecklistItem(it.description, it.completed) },
            createdAt = document.createdAt,
            updatedAt = document.updatedAt
        )
    }

    fun toDocument(entity: Note): NoteDocument {
        return NoteDocument(
            id = entity.id,
            userId = entity.userId,
            title = entity.title,
            content = entity.content,
            tags = entity.tags,
            color = entity.color,
            pinned = entity.pinned,
            archived = entity.archived,
            checklist = entity.checklist.map { ChecklistItemDocument(it.description, it.completed) },
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}
