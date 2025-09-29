package com.rubenslavor.keeplite.application.rest.note.mapper

import com.rubenslavor.keeplite.application.rest.note.request.ChecklistItemRequest
import com.rubenslavor.keeplite.application.rest.note.request.NoteRequest
import com.rubenslavor.keeplite.application.rest.note.response.ChecklistItemResponse
import com.rubenslavor.keeplite.application.rest.note.response.NoteResponse
import com.rubenslavor.keeplite.domain.note.model.ChecklistItemModel
import com.rubenslavor.keeplite.domain.note.model.NoteModel

object NoteApiMapper {
    fun toModel(request: NoteRequest) = NoteModel(
        title = request.title,
        content = request.content,
        tags = request.tags ?: emptyList(),
        color = request.color,
        pinned = request.pinned ?: false,
        archived = request.archived ?: false,
        checklist = request.checklist?.map { toModel(it) } ?: emptyList()
    )

    fun toResponse(model: NoteModel) = NoteResponse(
        id = model.id,
        userId = model.userId,
        title = model.title,
        content = model.content,
        tags = model.tags,
        color = model.color,
        pinned = model.pinned,
        archived = model.archived,
        checklist = model.checklist.map { toResponse(it) },
        createdAt = model.createdAt,
        updatedAt = model.updatedAt
    )

    private fun toModel(request: ChecklistItemRequest) = ChecklistItemModel(
        description = request.description,
        completed = request.completed ?: false
    )

    private fun toResponse(model: ChecklistItemModel) = ChecklistItemResponse(
        description = model.description,
        completed = model.completed
    )
}