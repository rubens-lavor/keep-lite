package com.rubenslavor.keeplite.domain.note.model

import java.time.LocalDateTime

data class NoteModel(
    val id: String? = null,
    val userId: String,
    val title: String,
    val content: String,
    val tags: List<String> = emptyList(),
    val color: String? = null,
    val pinned: Boolean = false,
    val archived: Boolean = false,
    val checklist: List<ChecklistItemModel> = emptyList(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)