package com.rubenslavor.keeplite.application.rest.note.request

import jakarta.validation.constraints.NotBlank

data class ChecklistItemRequest(
    @field:NotBlank
    val text: String,
    val done: Boolean = false
)
