package com.rubenslavor.keeplite.application.rest.note.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class NoteRequest(
    @field:NotBlank
    val title: String,

    @field:Size(max = 5000)
    val content: String = "",

    val tags: List<String> = emptyList(),
    val color: String? = null, // hex opcional, ex: #FF5733
    val pinned: Boolean = false,
    val archived: Boolean = false,
    val checklist: List<ChecklistItemRequest> = emptyList()
)
