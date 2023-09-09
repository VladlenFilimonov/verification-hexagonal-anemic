package com.examples.verification.domain.service

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.ConfirmVerificationResult
import com.examples.verification.domain.port.outbound.ConfirmVerificationEventPort
import com.examples.verification.domain.service.rules.VerificationBusinessRulesService
import com.examples.verification.domain.validation.VerificationValidationService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ConfirmVerificationFacade(
    private val validationService: VerificationValidationService,
    private val businessRulesService: VerificationBusinessRulesService,
    private val confirmVerificationEventPort: ConfirmVerificationEventPort,
    private val confirmVerificationService: ConfirmVerificationService
) {
    fun confirm(command: ConfirmVerificationCommand): Mono<ConfirmVerificationResult> {
        return validationService.validate(command)
            .flatMap(businessRulesService::applyRules)
            .flatMap(confirmVerificationService::confirm)
            .flatMap(confirmVerificationEventPort::send)
            .map { ConfirmVerificationResult(true) }
    }
}
