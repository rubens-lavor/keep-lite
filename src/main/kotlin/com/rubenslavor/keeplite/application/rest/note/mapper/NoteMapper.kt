package com.rubenslavor.keeplite.application.rest.note.mapper

import com.rubenslavor.keeplite.application.rest.note.request.ChecklistItemRequest
import com.rubenslavor.keeplite.application.rest.note.request.NoteRequest
import com.rubenslavor.keeplite.application.rest.note.response.ChecklistItemResponse
import com.rubenslavor.keeplite.application.rest.note.response.NoteResponse
import com.rubenslavor.keeplite.domain.note.model.ChecklistItemModel
import com.rubenslavor.keeplite.domain.note.model.NoteModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object NoteMapper {

    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    fun toDTO(request: NoteRequest, userId: String): NoteModel =
        NoteModel(
            id = null,
            userId = userId,
            title = request.title,
            content = request.content,
            tags = request.tags,
            color = request.color,
            pinned = request.pinned,
            archived = request.archived,
            checklist = request.checklist.map { it.toDTO() },
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

    fun toVO(dto: NoteModel): NoteResponse =
        NoteResponse(
            id = dto.id ?: "",
            title = dto.title,
            content = dto.content,
            tags = dto.tags,
            color = dto.color,
            pinned = dto.pinned,
            archived = dto.archived,
            checklist = dto.checklist.map { it.toVO() },
            createdAt = dto.createdAt.format(formatter),
            updatedAt = dto.updatedAt.format(formatter)
        )

    private fun ChecklistItemRequest.toDTO() = ChecklistItemModel(text = this.text, done = this.done)

    private fun ChecklistItemModel.toVO() = ChecklistItemResponse(text = this.text, done = this.done)
}
