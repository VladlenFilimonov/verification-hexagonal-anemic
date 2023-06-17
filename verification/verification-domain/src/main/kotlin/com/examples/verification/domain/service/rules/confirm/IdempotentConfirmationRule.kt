package com.examples.verification.domain.service.rules.confirm

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.port.outbound.AcquireDistributedLockPort
import com.examples.verification.domain.utils.aop.Rule
import reactor.core.publisher.Mono

@Rule
class IdempotentConfirmationRule(
    private val acquireDistributedLockPort: AcquireDistributedLockPort
) {

    fun apply(cmd: ConfirmVerificationCommand): Mono<ConfirmVerificationCommand> {
        return acquireDistributedLockPort.acquire(cmd)
            .switchIfEmpty(buildVerificationError(null))
            .onErrorResume { buildVerificationError(it) }
    }

    private fun buildVerificationError(error: Throwable?): Mono<out ConfirmVerificationCommand> {
        return Mono.error(
            VerificationError(
                "Verification lock acquiring failed",
                ErrorCode.VERIFICATION_IDEMPOTENT_RULE_VIOLATION,
                error
            )
        )
    }
}