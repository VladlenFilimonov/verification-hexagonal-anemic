package com.examples.verification.out.redis.model

import java.time.OffsetDateTime
import java.util.UUID

data class VerificationAttemptRedisModel(
    val id: String,
    val verificationId: UUID,
    val createdAt: OffsetDateTime
)