package com.rubenslavor.keeplite.domain.note.usecase

import java.util.UUID

interface DeleteNoteUseCase {
    fun delete(id: UUID)
}