package com.examples.verification.domain.service.rules

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.ReadVerificationPort
import com.examples.verification.domain.service.rules.BusinessRuleOrder.VERIFICATION_EXPIRATION_RULE
import com.examples.verification.domain.utils.aop.Rule
import reactor.core.publisher.Mono

@Rule
class VerificationExpirationRule(
    private val readVerificationPort: ReadVerificationPort
) : BusinessRule<ConfirmVerificationCommand> {
    override fun apply(cmd: ConfirmVerificationCommand): Mono<ConfirmVerificationCommand> {
        return readVerificationPort.read(cmd.id)
            .doOnNext { verification -> checkForVerificationExpired(verification) }
            .map { _ -> cmd }
            .switchIfEmpty(throwVerificationNotFoundError(cmd))
    }

    override fun getOrder(): Int {
        return VERIFICATION_EXPIRATION_RULE.order
    }

    private fun checkForVerificationExpired(verification: Verification) {
        verification.expired ?: throw VerificationError(
            "Verification expired with id: ${verification.id}",
            ErrorCode.VERIFICATION_EXPIRED
        )
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