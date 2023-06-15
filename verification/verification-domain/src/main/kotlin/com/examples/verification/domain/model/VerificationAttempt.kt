package com.examples.verification.domain.model

import java.time.OffsetDateTime
import java.util.UUID

data class VerificationAttempt(
    val verificationId: UUID,
    val createdAt: OffsetDateTime
)

data class VerificationAttempts(
    val attempts: List<VerificationAttempt>
)