package com.rubenslavor.keeplite.application.rest.note.request

import jakarta.validation.constraints.NotBlank

data class ChecklistItemRequest(
    @param:NotBlank val description: String,
    val completed: Boolean? = false
)
