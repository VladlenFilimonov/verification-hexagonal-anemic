package com.examples.verification.domain.config

import java.time.Duration


class ApplicationProperties(
    val verificationTtl: Duration,
    val verificationMaxAttempts: Int,
    val verificationCodeLength: Int,
    val requestTimeout: Duration
) {
    constructor() : this(
        Duration.parse("5m"),
        5,
        8,
        Duration.parse("10s")
    )
}