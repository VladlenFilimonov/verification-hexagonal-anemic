package com.examples.verification.domain.service.rules.confirm

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.config.ApplicationProperties
import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.VerificationAttempts
import com.examples.verification.domain.port.outbound.ReadVerificationAttemptsPort
import com.examples.verification.domain.utils.aop.Rule
import reactor.core.publisher.Mono

@Rule
class MaxAttemptsConfirmationRule(
    private val applicationProperties: ApplicationProperties,
    private val readVerificationAttemptsPort: ReadVerificationAttemptsPort
) {

    fun apply(cmd: ConfirmVerificationCommand): Mono<ConfirmVerificationCommand> {
        return readVerificationAttemptsPort.read(cmd.id)
            .map { checkForMaxAttempts(it, cmd) }
            .switchIfEmpty(Mono.error(VerificationError("Verification not found", ErrorCode.VERIFICATION_NOT_FOUND)))
    }

    private fun checkForMaxAttempts(
        verificationAttempts: VerificationAttempts,
        cmd: ConfirmVerificationCommand
    ): ConfirmVerificationCommand {
        if (verificationAttempts.attempts.size > applicationProperties.verificationMaxAttempts) {
            throw VerificationError(
                "Verification expired by max attempt rule",
                ErrorCode.VERIFICATION_EXPIRED_BY_MAX_ATTEMPTS
            )
        }
        return cmd;
    }
}