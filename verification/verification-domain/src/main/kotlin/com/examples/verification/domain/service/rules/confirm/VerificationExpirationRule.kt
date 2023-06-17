package com.examples.verification.domain.service.rules.confirm

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.ReadVerificationPort
import com.examples.verification.domain.utils.aop.Rule
import reactor.core.publisher.Mono

@Rule
class VerificationExpirationRule(
    private val readVerificationPort: ReadVerificationPort
) {
    fun apply(cmd: ConfirmVerificationCommand): Mono<ConfirmVerificationCommand> {
        return readVerificationPort.read(cmd.id)
            .map { checkForVerificationExpired(it, cmd) }
            .switchIfEmpty(throwVerificationNotFoundError(cmd))
    }

    private fun checkForVerificationExpired(
        verification: Verification,
        cmd: ConfirmVerificationCommand
    ): ConfirmVerificationCommand {

        if (verification.expired) {
            throw VerificationError(
                "Verification expired with id: ${verification.id}",
                ErrorCode.VERIFICATION_EXPIRED
            )
        }
        return cmd
    }

    private fun throwVerificationNotFoundError(cmd: ConfirmVerificationCommand): Mono<out ConfirmVerificationCommand> {
        return Mono.error(
            VerificationError(
                "Verification not found with id: ${cmd.id}",
                ErrorCode.VERIFICATION_NOT_FOUND
            )
        )
    }

}