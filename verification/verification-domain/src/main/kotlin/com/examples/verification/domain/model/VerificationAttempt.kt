package com.examples.verification.domain.model

import java.time.Instant
import java.util.UUID

data class VerificationAttempt(
    val verificationId: UUID,
    val createdAt: Instant
)

data class VerificationAttempts(
    val attempts: List<VerificationAttempt>
)