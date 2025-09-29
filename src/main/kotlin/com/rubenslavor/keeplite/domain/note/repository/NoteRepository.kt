package com.rubenslavor.keeplite.domain.note.repository

import com.rubenslavor.keeplite.domain.note.entity.Note
import java.util.UUID

interface NoteRepository {
    fun save(note: Note): Note
    fun findByIdAndUserId(id: UUID, userId: UUID): Note?
    fun findAllByUserId(userId: UUID): List<Note>
    fun deleteByIdAndUserId(id: UUID, userId: UUID)
}