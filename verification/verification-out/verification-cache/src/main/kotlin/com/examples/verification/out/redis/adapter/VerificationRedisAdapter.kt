package com.examples.verification.out.redis.adapter

import com.examples.verification.domain.config.ApplicationProperties
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.model.VerificationAttempt
import com.examples.verification.domain.model.VerificationAttempts
import com.examples.verification.domain.port.outbound.ReadVerificationAttemptsPort
import com.examples.verification.domain.port.outbound.SaveVerificationAttemptsPort
import com.examples.verification.domain.utils.aop.Adapter
import com.examples.verification.out.redis.model.VerificationAttemptRedisModel
import java.util.UUID
import org.springframework.core.convert.ConversionService
import org.springframework.data.redis.core.ReactiveRedisTemplate
import reactor.core.publisher.Mono

@Adapter
class VerificationRedisAdapter(
    private val redisTemplate: ReactiveRedisTemplate<String, VerificationAttemptRedisModel>,
    private val conversionService: ConversionService,
    private val applicationProperties: ApplicationProperties
) : ReadVerificationAttemptsPort, SaveVerificationAttemptsPort {

    override fun read(verificationId: UUID): Mono<VerificationAttempts> {
        return redisTemplate
            .opsForSet()
            .members(KEY_PREFIX + verificationId.toString())
            .map { conversionService.convert(it, VerificationAttempt::class.java) }
            .collectList()
            .map { VerificationAttempts(it) }
    }

    override fun save(verification: Verification): Mono<Verification> {
        return Mono.fromSupplier { conversionService.convert(verification, VerificationAttemptRedisModel::class.java) }
            .flatMap { pushToRedis(it) }
            .map { verification }
    }

    private fun pushToRedis(verificationAttemptRedisModel: VerificationAttemptRedisModel): Mono<Boolean> {
        return redisTemplate
            .opsForSet()
            .add(KEY_PREFIX + verificationAttemptRedisModel.id, verificationAttemptRedisModel)
            .then(
                redisTemplate.expire(
                    KEY_PREFIX + verificationAttemptRedisModel.id,
                    applicationProperties.verificationAttemptsTtl
                )
            )
    }

    private companion object {
        const val KEY_PREFIX = "verification:attempts:"
    }
}