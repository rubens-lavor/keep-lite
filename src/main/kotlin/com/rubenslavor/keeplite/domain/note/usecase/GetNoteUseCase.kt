package com.rubenslavor.keeplite.domain.note.usecase

import com.rubenslavor.keeplite.domain.note.model.NoteModel
import java.util.UUID

interface GetNoteUseCase {
    fun get(id: UUID): NoteModel
    fun list(): List<NoteModel>
}