package com.rubenslavor.keeplite.application.service

import com.rubenslavor.keeplite.application.service.mapper.NoteDomainMapper
import com.rubenslavor.keeplite.domain.exceptions.BusinessException
import com.rubenslavor.keeplite.domain.note.model.NoteModel
import com.rubenslavor.keeplite.domain.note.repository.NoteRepository
import com.rubenslavor.keeplite.domain.note.usecase.CreateNoteUseCase
import com.rubenslavor.keeplite.domain.note.usecase.DeleteNoteUseCase
import com.rubenslavor.keeplite.domain.note.usecase.GetNoteUseCase
import com.rubenslavor.keeplite.domain.note.usecase.UpdateNoteUseCase
import com.rubenslavor.keeplite.domain.user.entity.User
import com.rubenslavor.keeplite.domain.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class NoteServiceImpl(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository
) : CreateNoteUseCase, GetNoteUseCase, UpdateNoteUseCase, DeleteNoteUseCase {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun create(note: NoteModel): NoteModel {
        log.info("Criando nota: {}", note)

        val currentUser = getAuthenticatedUser()
        val noteEntity = NoteDomainMapper.toEntity(note, currentUser.id) // Passa o ID do usuário logado
        val savedNote = noteRepository.save(noteEntity)
        return NoteDomainMapper.toModel(savedNote).also {
            log.info("Nota criada com sucesso id: {}", it.id)
        }
    }

    override fun get(id: UUID): NoteModel {
        log.info("Buscando nota id: {}", id)

        val currentUser = getAuthenticatedUser()
        val note = noteRepository.findByIdAndUserId(id, currentUser.id)
            ?: throw BusinessException("Nota não encontrada ou não pertence ao usuário.")
        return NoteDomainMapper.toModel(note).also {
            log.info("Nota recuperada com sucesso id: {}", it.id)
        }
    }

    override fun list(): List<NoteModel> {
        log.info("Buscando lista de notas")

        val currentUser = getAuthenticatedUser()
        val notes = noteRepository.findAllByUserId(currentUser.id)
        return notes.map { NoteDomainMapper.toModel(it) }.also {
            log.info("Lista recuperada com sucesso")
        }
    }

    override fun update(id: UUID, note: NoteModel): NoteModel {
        log.info("Atualizando nota id: {}", id)

        val currentUser = getAuthenticatedUser()
        val existingNote = noteRepository.findByIdAndUserId(id, currentUser.id)
            ?: throw BusinessException("Nota não encontrada ou não pertence ao usuário.")

        val updatedNote = existingNote.copy(
            title = note.title,
            content = note.content,
            tags = note.tags,
            color = note.color,
            pinned = note.pinned,
            archived = note.archived,
            checklist = note.checklist.map { NoteDomainMapper.toEntity(it) },
            updatedAt = LocalDateTime.now()
        )

        val savedNote = noteRepository.save(updatedNote)
        return NoteDomainMapper.toModel(savedNote).also {
            log.info("Nota atualizada com sucesso id: {}", it.id)
        }
    }

    override fun delete(id: UUID) {
        log.info("Deletando nota id: {}", id)

        val currentUser = getAuthenticatedUser()
        noteRepository.deleteByIdAndUserId(id, currentUser.id).also {
            log.info("Nota deletada com sucesso")
        }
    }

    private fun getAuthenticatedUser(): User {
        val email = SecurityContextHolder.getContext().authentication.name
        return userRepository.findByEmail(email)
            ?: throw BusinessException("Usuário autenticado não encontrado no sistema.")
    }
}