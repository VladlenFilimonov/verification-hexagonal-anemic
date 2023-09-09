package com.examples.verification.out.redis.converter

import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.model.VerificationAttempt
import com.examples.verification.out.redis.model.VerificationAttemptRedisModel
import java.time.OffsetDateTime
import java.util.UUID
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class VerificationAttemptToModelConverter : Converter<Verification, VerificationAttemptRedisModel> {
    override fun convert(source: Verification): VerificationAttemptRedisModel {
        return VerificationAttemptRedisModel(
            id = UUID.randomUUID().toString(),
            verificationId = source.id ?: throw IllegalArgumentException("No verification id found"),
            createdAt = OffsetDateTime.now()
        )
    }
}

@Component
class ModelToVerificationAttemptConverter : Converter<VerificationAttemptRedisModel, VerificationAttempt> {
    override fun convert(source: VerificationAttemptRedisModel): VerificationAttempt {
        return VerificationAttempt(
            verificationId = source.verificationId,
            createdAt = source.createdAt
        )
    }
}