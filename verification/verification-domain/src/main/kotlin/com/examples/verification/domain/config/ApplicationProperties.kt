package com.examples.verification.domain.config

import java.time.Duration
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "verification")
class ApplicationProperties(
    val verificationTtl: Duration,
    val verificationMaxAttempts: Int,
    val verificationCodeLength: Int,
    val requestTimeout: Duration,
    val verificationAttemptsTtl: Duration,
    val confirmVerificationLockTtl: Duration
)