package com.examples.verification.domain.config

import kotlin.time.Duration

class ApplicationProperties(
    val verificationTtl: Duration,
    val verificationMaxAttempts: Int,
    val verificationCodeLength: Int,
    val requestTimeout: java.time.Duration
) {
    constructor() : this(
        Duration.parse("5m"),
        5,
        8,
        java.time.Duration.parse("10s")
    )
}