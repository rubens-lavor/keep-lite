package com.rubenslavor.keeplite.application.rest.note.controller.v1

import com.rubenslavor.keeplite.application.rest.note.mapper.NoteApiMapper
import com.rubenslavor.keeplite.application.rest.note.request.NoteRequest
import com.rubenslavor.keeplite.application.rest.note.response.NoteResponse
import com.rubenslavor.keeplite.domain.note.usecase.CreateNoteUseCase
import com.rubenslavor.keeplite.domain.note.usecase.DeleteNoteUseCase
import com.rubenslavor.keeplite.domain.note.usecase.GetNoteUseCase
import com.rubenslavor.keeplite.domain.note.usecase.UpdateNoteUseCase
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.UUID

@Tag(name = "Note Controller")
@RestController
@RequestMapping("/api/v1/notes")
class NoteController(
    private val createNoteUseCase: CreateNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) {

    @PostMapping
    fun create(@Valid @RequestBody request: NoteRequest): ResponseEntity<NoteResponse> {
        val noteModel = NoteApiMapper.toModel(request)
        val createdNote = createNoteUseCase.create(noteModel)
        val response = NoteApiMapper.toResponse(createdNote)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(response.id).toUri()

        return ResponseEntity.created(location).body(response)
    }

    @GetMapping
    fun list(): ResponseEntity<List<NoteResponse>> {
        val notes = getNoteUseCase.list()
        val response = notes.map { NoteApiMapper.toResponse(it) }
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<NoteResponse> {
        val note = getNoteUseCase.get(id)
        val response = NoteApiMapper.toResponse(note)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody request: NoteRequest): ResponseEntity<NoteResponse> {
        val noteModel = NoteApiMapper.toModel(request)
        val updatedNote = updateNoteUseCase.update(id, noteModel)
        val response = NoteApiMapper.toResponse(updatedNote)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        deleteNoteUseCase.delete(id)
        return ResponseEntity.noContent().build()
    }
}
