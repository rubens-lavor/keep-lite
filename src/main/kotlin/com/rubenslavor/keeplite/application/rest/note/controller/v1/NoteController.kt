package com.rubenslavor.keeplite.application.rest.note.controller.v1

import com.rubenslavor.keeplite.application.rest.note.mapper.NoteMapper
import com.rubenslavor.keeplite.application.rest.note.request.NoteRequest
import com.rubenslavor.keeplite.application.rest.note.response.NoteResponse
import com.rubenslavor.keeplite.domain.note.model.NoteModel
//import com.rubenslavor.keeplite.domain.note.usecase.CreateNoteUseCase
//import com.rubenslavor.keeplite.domain.note.usecase.DeleteNoteUseCase
//import com.rubenslavor.keeplite.domain.note.usecase.GetNoteUseCase
//import com.rubenslavor.keeplite.domain.note.usecase.ListNotesUseCase
//import com.rubenslavor.keeplite.domain.note.usecase.UpdateNoteUseCase
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/v1/notes")
class NoteController(
//    private val createNote: CreateNoteUseCase,
//    private val updateNote: UpdateNoteUseCase,
//    private val deleteNote: DeleteNoteUseCase,
//    private val getNote: GetNoteUseCase,
//    private val listNotes: ListNotesUseCase
) {
//
//    @GetMapping
//    fun list(
//        @RequestParam(required = false) title: String?,
//        @RequestParam(defaultValue = "0") page: Int,
//        @RequestParam(defaultValue = "10") size: Int
//    ): ResponseEntity<Page<NoteResponse>> {
//        val notes = listNotes.execute(title, page, size)
//        val body = notes.map { NoteMapper.toVO(it) }
//        return ResponseEntity.ok(body)
//    }
//
//    @GetMapping("/{id}")
//    fun get(@PathVariable id: String): ResponseEntity<NoteResponse> {
//        val note: NoteModel = getNote.execute(id) ?: return ResponseEntity.notFound().build()
//        return ResponseEntity.ok(NoteMapper.toVO(note))
//    }
//
//    @PostMapping
//    fun create(
//        @Valid @RequestBody req: NoteRequest,
//        authentication: Authentication
//    ): ResponseEntity<NoteResponse> {
//        val userId = authentication.name
//        val created: NoteModel = createNote.execute(NoteMapper.toDTO(request = req, userId = userId))
//        val location = URI.create("/api/v1/notes/${created.id}")
//        return ResponseEntity.created(location).body(NoteMapper.toVO(created))
//    }
//
//    @PutMapping("/{id}")
//    fun update(@PathVariable id: String, @Valid @RequestBody req: NoteRequest): ResponseEntity<NoteResponse> {
//        val updated: NoteModel = updateNote.execute(id, NoteMapper.toDTO(req, id)) ?: return ResponseEntity.notFound().build()
//        return ResponseEntity.ok(NoteMapper.toVO(updated))
//    }
//
//    @DeleteMapping("/{id}")
//    fun delete(@PathVariable id: String): ResponseEntity<Void> {
//        val deleted: Boolean = deleteNote.execute(id)
//        return if (deleted) ResponseEntity.noContent().build()
//        else ResponseEntity.notFound().build()
//    }
}
