package com.examples.verification.domain.service.rules.create

import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.ReadVerificationPort
import com.examples.verification.domain.utils.aop.Rule
import reactor.core.publisher.Mono

@Rule
class DuplicationVerificationRule(
    private val readVerificationPort: ReadVerificationPort
) {
    fun apply(cmd: CreateVerificationCommand): Mono<CreateVerificationCommand> {
        return readVerificationPort.read(cmd.subject)
            .map { verification -> checkForDuplication(verification, cmd) }
            .switchIfEmpty(Mono.just(cmd))
    }

    private fun checkForDuplication(
        verification: Verification,
        cmd: CreateVerificationCommand
    ): CreateVerificationCommand {

        if (!verification.expired) {
            throw VerificationError("Duplication verification error", ErrorCode.VERIFICATION_DUPLICATION)
        }
        return cmd;
    }
}