package com.rubenslavor.keeplite.infrastructure.config

import com.rubenslavor.keeplite.domain.user.entity.User
import com.rubenslavor.keeplite.domain.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminUserInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    @param:Value("\${admin.email}") private val adminEmail: String,
    @param:Value("\${admin.password}") private val adminPass: String
) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String?) {
        if (userRepository.findByEmail(adminEmail) == null) {
            log.info("Nenhum usuário administrador encontrado. Criando usuário admin semente...")

            val adminUser = User(
                name = "Admin User",
                email = adminEmail,
                password = passwordEncoder.encode(adminPass),
                roles = setOf("USER", "ADMIN") // Atribui as roles USER e ADMIN
            )
            userRepository.save(adminUser)
            log.info("Usuário administrador criado com sucesso!")
        } else {
            log.info("Usuário administrador já existe. Nenhuma ação necessária.")
        }
    }
}