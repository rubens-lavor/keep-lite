package com.rubenslavor.keeplite.application.rest.note.response

data class NoteResponse(
    val id: String,
    val title: String,
    val content: String,
    val tags: List<String>,
    val color: String?,
    val pinned: Boolean,
    val archived: Boolean,
    val checklist: List<ChecklistItemResponse>,
    val createdAt: String,
    val updatedAt: String
)
