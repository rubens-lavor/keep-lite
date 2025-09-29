package com.rubenslavor.keeplite.domain.note.usecase

import com.rubenslavor.keeplite.domain.note.model.NoteModel
import java.util.UUID

interface UpdateNoteUseCase {
    fun update(id: UUID, note: NoteModel): NoteModel
}