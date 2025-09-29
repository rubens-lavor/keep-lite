package com.rubenslavor.keeplite.application.service.mapper

import com.rubenslavor.keeplite.domain.note.entity.ChecklistItem
import com.rubenslavor.keeplite.domain.note.entity.Note
import com.rubenslavor.keeplite.domain.note.model.ChecklistItemModel
import com.rubenslavor.keeplite.domain.note.model.NoteModel
import java.util.UUID

object NoteDomainMapper {
    fun toEntity(model: NoteModel, userId: UUID): Note {
        return Note(
            userId = userId,
            title = model.title,
            content = model.content,
            tags = model.tags,
            color = model.color,
            pinned = model.pinned,
            archived = model.archived,
            checklist = model.checklist.map { toEntity(model = it) }
        )
    }

    fun toEntity(model: ChecklistItemModel): ChecklistItem {
        return ChecklistItem(
            description = model.description,
            completed = model.completed
        )
    }

    fun toModel(entity: Note): NoteModel {
        return NoteModel(
            id = entity.id,
            userId = entity.userId,
            title = entity.title,
            content = entity.content,
            tags = entity.tags,
            color = entity.color,
            pinned = entity.pinned,
            archived = entity.archived,
            checklist = entity.checklist.map { toModel(entity = it) },
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toModel(entity: ChecklistItem): ChecklistItemModel {
        return ChecklistItemModel(
            description = entity.description,
            completed = entity.completed
        )
    }
}
