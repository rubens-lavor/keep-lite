package com.rubenslavor.keeplite.application.rest.user.controller.v1

import com.rubenslavor.keeplite.application.rest.user.request.RegisterRequest
import com.rubenslavor.keeplite.application.rest.user.response.UserResponse
import com.rubenslavor.keeplite.application.rest.user.mapper.UserMapper
import com.rubenslavor.keeplite.domain.user.model.UserModel
import com.rubenslavor.keeplite.domain.user.usecase.RegisterUserUseCase
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val registerUser: RegisterUserUseCase
) {

    @PostMapping("/register", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<UserResponse> {

        val register = UserMapper.toModel(request)
        val createdUser = registerUser.execute(register)

        val location = uRI(createdUser)
        val body: UserResponse = UserMapper.toResponse(user = createdUser)

        return ResponseEntity.created(location).body(body)
    }

    private fun uRI(createdUser: UserModel): URI = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("/api/v1/user/{id}")
        .buildAndExpand(createdUser.id)
        .toUri()
}