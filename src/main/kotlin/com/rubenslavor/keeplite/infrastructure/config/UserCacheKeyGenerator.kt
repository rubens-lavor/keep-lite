package com.rubenslavor.keeplite.infrastructure.config

import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component("userCacheKeyGenerator")
class UserCacheKeyGenerator : KeyGenerator {

    override fun generate(target: Any, method: Method, vararg params: Any?): String {
        // Retorna o email (authentication.name) como a chave do cache
        return SecurityContextHolder.getContext().authentication.name
    }
}