package com.examples.verification.domain.config

import java.time.Duration


class ApplicationProperties(
    val verificationTtl: Duration,
    val verificationMaxAttempts: Int,
    val verificationCodeLength: Int,
    val requestTimeout: Duration,
    val verificationAttemptsTtl: Duration,
    val confirmVerificationLockTtl: Duration
) {
    constructor() : this(
        Duration.parse("PT5M"),
        5,
        8,
        Duration.parse("PT10S"),
        Duration.parse("P2D"),
        Duration.parse("PT15S")
    )
}