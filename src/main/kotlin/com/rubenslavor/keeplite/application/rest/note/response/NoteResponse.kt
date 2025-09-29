package com.rubenslavor.keeplite.application.rest.note.response

import java.time.LocalDateTime
import java.util.UUID

data class NoteResponse(
    val id: UUID?,
    val userId: UUID?,
    val title: String?,
    val content: String,
    val tags: List<String>,
    val color: String?,
    val pinned: Boolean,
    val archived: Boolean,
    val checklist: List<ChecklistItemResponse>,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
