package com.rubenslavor.keeplite.domain.note.entity

data class ChecklistItem(
    val description: String,
    val completed: Boolean = false
)