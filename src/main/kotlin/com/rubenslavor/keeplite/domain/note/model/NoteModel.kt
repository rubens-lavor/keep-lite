package com.rubenslavor.keeplite.domain.note.model

import java.time.LocalDateTime
import java.util.UUID

data class NoteModel(
    val id: UUID? = null,
    val userId: UUID? = null,
    val title: String?,
    val content: String,
    val tags: List<String> = emptyList(),
    val color: String? = null,
    val pinned: Boolean = false,
    val archived: Boolean = false,
    val checklist: List<ChecklistItemModel> = emptyList(),
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)