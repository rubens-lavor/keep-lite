package com.rubenslavor.keeplite.infrastructure.persistence.mongo.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document("notes")
data class NoteDocument(
    @Id
    val id: UUID,
    val userId: UUID,
    val title: String?,
    val content: String,
    val tags: List<String>,
    val color: String?,
    val pinned: Boolean,
    val archived: Boolean,
    val checklist: List<ChecklistItemDocument>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    // Classe aninhada para representar o sub-documento do checklist
    data class ChecklistItemDocument(
        val description: String,
        val completed: Boolean
    )
}