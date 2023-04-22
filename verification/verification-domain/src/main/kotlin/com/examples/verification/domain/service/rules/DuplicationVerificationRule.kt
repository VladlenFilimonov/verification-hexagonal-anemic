package com.examples.verification.domain.service.rules

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
) : BusinessRule<CreateVerificationCommand> {

    override fun apply(cmd: CreateVerificationCommand): Mono<CreateVerificationCommand> {
        return readVerificationPort.read(cmd.subject)
            .map { verification -> checkForDuplication(verification, cmd) }
            .switchIfEmpty(Mono.just(cmd))
    }

    override fun getOrder(): Int {
        return BusinessRuleOrder.VERIFICATION_DUPLICATION_RULE.order;
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