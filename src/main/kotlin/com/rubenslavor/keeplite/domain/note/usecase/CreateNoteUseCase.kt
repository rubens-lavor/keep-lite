package com.rubenslavor.keeplite.domain.note.usecase

import com.rubenslavor.keeplite.domain.note.model.NoteModel

interface CreateNoteUseCase {
    fun create(note: NoteModel): NoteModel
}