package com.examples.verification.domain.service.rules

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.service.rules.confirm.MaxAttemptsConfirmationRule
import com.examples.verification.domain.service.rules.confirm.UserInfoMatchingRule
import com.examples.verification.domain.service.rules.confirm.VerificationExpirationRule
import com.examples.verification.domain.service.rules.create.DuplicationVerificationRule
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class VerificationBusinessRulesService(
    private val duplicationVerificationRule: DuplicationVerificationRule,
    private val userInfoMatchingRule: UserInfoMatchingRule,
    private val verificationExpirationRule: VerificationExpirationRule,
    private val maxAttemptsConfirmationRule: MaxAttemptsConfirmationRule
) {
    fun applyRules(cmd: CreateVerificationCommand): Mono<CreateVerificationCommand> {
        return duplicationVerificationRule.apply(cmd)
    }

    fun applyRules(cmd: ConfirmVerificationCommand): Mono<ConfirmVerificationCommand> {
        return userInfoMatchingRule.apply(cmd)
            .flatMap { command -> verificationExpirationRule.apply(command) }
            .flatMap { command -> maxAttemptsConfirmationRule.apply(command) }
    }

}
