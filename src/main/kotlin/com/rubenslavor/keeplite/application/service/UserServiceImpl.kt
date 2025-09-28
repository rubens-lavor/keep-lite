package com.rubenslavor.keeplite.application.service

import com.rubenslavor.keeplite.application.service.mapper.UserDomainMapper
import com.rubenslavor.keeplite.domain.user.model.RegisterModel
import com.rubenslavor.keeplite.domain.user.model.UserModel
import com.rubenslavor.keeplite.domain.user.entity.User
import com.rubenslavor.keeplite.domain.exceptions.BusinessException
import com.rubenslavor.keeplite.domain.user.repository.UserRepository
import com.rubenslavor.keeplite.domain.user.usecase.RegisterUserUseCase
import org.springframework.security.crypto.password.PasswordEncoder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : RegisterUserUseCase {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun execute(register: RegisterModel): UserModel {
        log.info("Iniciando processo de registro para o e-mail: {}", register.email)

        userRepository.findByEmail(register.email)?.let {
            log.warn("Tentativa de registro com e-mail já existente: {}", register.email)
            throw BusinessException(message = "E-mail já está em uso")
        }

        val user = User(
            name = register.name,
            email = register.email,
            password = passwordEncoder.encode(register.password) // hash da senha
        )

        val savedUser = userRepository.save(user)

        log.info("Usuário registrado com sucesso. ID: {}", savedUser.id)
        return UserDomainMapper.toModel(savedUser)
    }
}
