package com.rubenslavor.keeplite.infrastructure.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import java.time.Duration

@Configuration
@EnableCaching
class CachingConfig {
    @Bean
    fun redisCacheManagerBuilderCustomizer(objectMapper: ObjectMapper): RedisCacheManagerBuilderCustomizer {
        // 1. Criamos uma cópia do ObjectMapper principal do Spring para customizá-lo
        val mapper = objectMapper.copy()
        // 2. Ativamos a "tipagem padrão". Isso adiciona uma propriedade "@class" ao JSON,
        //    garantindo que o Jackson saiba como converter o JSON de volta para a nossa List<NoteModel>
        mapper.activateDefaultTyping(mapper.polymorphicTypeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)

        // 3. Criamos o serializador USANDO o nosso mapper configurado
        val serializer = GenericJackson2JsonRedisSerializer(mapper)

        return RedisCacheManagerBuilderCustomizer { builder ->
            builder.withCacheConfiguration(
                "notesByUserId",
                RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(10))
                    .serializeValuesWith(SerializationPair.fromSerializer(serializer)) // Usa o novo serializador
            )
        }
    }
}
