package com.examples.verification.out.redis.config

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.out.redis.model.VerificationAttemptRedisModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
open class RedisConfig {

    @Bean
    open fun verificationAttemptRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, VerificationAttemptRedisModel> {
        val serializer = Jackson2JsonRedisSerializer(buildObjectMapper(), VerificationAttemptRedisModel::class.java)
        val ctx = RedisSerializationContext
            .newSerializationContext<String, VerificationAttemptRedisModel>(StringRedisSerializer())
            .value(serializer)
            .build()
        return ReactiveRedisTemplate(factory, ctx)
    }

    @Bean
    open fun confirmVerificationRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, ConfirmVerificationCommand> {
        val serializer = Jackson2JsonRedisSerializer(buildObjectMapper(), ConfirmVerificationCommand::class.java)
        val ctx = RedisSerializationContext
            .newSerializationContext<String, ConfirmVerificationCommand>(StringRedisSerializer())
            .value(serializer)
            .build()
        return ReactiveRedisTemplate(factory, ctx)
    }

    private fun buildObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper().registerKotlinModule()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        return objectMapper
    }
}