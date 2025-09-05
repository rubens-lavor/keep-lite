package com.rubenslavor.keeplite.application.controller.v1

import com.rubenslavor.keeplite.application.controller.v1.request.NoteRequest
import com.rubenslavor.keeplite.application.controller.v1.vo.NoteVO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notes")
class NoteController(
    // aqui vai entrar os usecase
) {

    // TODO: elaborar melhor os endpoins, pensar nos filtros, requestBody e VO (response)

    // @GetMapping fun list(... filtros ...): Page<NoteOut>

    @GetMapping("/{id}") fun get(@PathVariable id: String): NoteVO {
        return NoteVO("hello world")
    }

    @PostMapping fun create(@Valid @RequestBody req: NoteRequest): ResponseEntity<NoteVO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(NoteVO(req.message))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @Valid @RequestBody req: NoteRequest): NoteVO {
        return NoteVO("hello world")
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: String) {}
}
