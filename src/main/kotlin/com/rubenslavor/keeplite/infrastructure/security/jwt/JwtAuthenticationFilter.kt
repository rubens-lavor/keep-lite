package com.rubenslavor.keeplite.infrastructure.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)
        val userEmail = jwtProvider.getEmailFromToken(jwt)

        // Se o token é válido E o usuário ainda não está autenticado no contexto de segurança
        if (SecurityContextHolder.getContext().authentication == null && jwtProvider.isTokenValid(jwt)) {
            val userDetails = this.userDetailsService.loadUserByUsername(userEmail)

            val authToken = UsernamePasswordAuthenticationToken(
                userDetails,
                null, // Credenciais são nulas pois já foram validadas pelo token
                userDetails.authorities
            )
            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

            // Coloca o usuário autenticado no contexto de segurança do Spring
            SecurityContextHolder.getContext().authentication = authToken
        }

        filterChain.doFilter(request, response)
    }
}