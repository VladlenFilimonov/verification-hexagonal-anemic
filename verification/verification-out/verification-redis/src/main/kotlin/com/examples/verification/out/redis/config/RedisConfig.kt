package com.examples.verification.out.redis.config

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.out.redis.model.VerificationAttemptRedisModel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun verificationAttemptRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, VerificationAttemptRedisModel> {
        val serializer = Jackson2JsonRedisSerializer(VerificationAttemptRedisModel::class.java)
        val ctx = RedisSerializationContext
            .newSerializationContext<String, VerificationAttemptRedisModel>(StringRedisSerializer())
            .value(serializer)
            .build()
        return ReactiveRedisTemplate(factory, ctx)
    }

    @Bean
    fun confirmVerificationRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, ConfirmVerificationCommand> {
        val serializer = Jackson2JsonRedisSerializer(ConfirmVerificationCommand::class.java)
        val ctx = RedisSerializationContext
            .newSerializationContext<String, ConfirmVerificationCommand>(StringRedisSerializer())
            .value(serializer)
            .build()
        return ReactiveRedisTemplate(factory, ctx)
    }
}