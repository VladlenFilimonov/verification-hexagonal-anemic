package com.examples.verification.out.redis.adapter

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.config.ApplicationProperties
import com.examples.verification.domain.port.outbound.AcquireDistributedLockPort
import com.examples.verification.domain.utils.aop.Adapter
import org.springframework.data.redis.core.ReactiveRedisTemplate
import reactor.core.publisher.Mono

@Adapter
class AcquireDistributedLockAdapter(
    private val redisTemplate: ReactiveRedisTemplate<String, ConfirmVerificationCommand>,
    private val applicationProperties: ApplicationProperties
) : AcquireDistributedLockPort {
    override fun acquire(cmd: ConfirmVerificationCommand): Mono<ConfirmVerificationCommand> {
        return redisTemplate
            .opsForValue()
            .setIfAbsent(KEY_PREFIX + cmd.id, cmd, applicationProperties.confirmVerificationLockTtl)
            .filter { it }
            .map { cmd }
    }

    private companion object {
        const val KEY_PREFIX = "verification:confirm:lock:"
    }
}