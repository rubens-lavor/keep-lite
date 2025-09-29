package com.rubenslavor.keeplite.application.rest.note.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class NoteRequest(
    val title: String?,
    @param:NotBlank
    @param:Size(max = 5000)
    val content: String,
    val tags: List<String>? = emptyList(),
    val color: String? = null,
    val pinned: Boolean? = false,
    val archived: Boolean? = false,
    val checklist: List<ChecklistItemRequest>? = emptyList()
)
