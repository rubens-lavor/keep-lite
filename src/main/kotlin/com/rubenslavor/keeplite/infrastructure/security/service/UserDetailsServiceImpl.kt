package com.rubenslavor.keeplite.infrastructure.security.service

import com.rubenslavor.keeplite.domain.user.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("Usuário com e-mail $username não encontrado.")

        return User.builder()
            .username(user.email)
            .password(user.password)
             .roles("USER")
            .build()
    }
}