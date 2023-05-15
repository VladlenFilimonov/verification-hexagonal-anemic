package com.examples.verification.domain.service

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.ConfirmVerificationResult
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.ConfirmEventVerificationPort
import com.examples.verification.domain.service.rules.VerificationBusinessRulesService
import com.examples.verification.domain.validation.VerificationValidationService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ConfirmVerificationFacade(
    private val validationService: VerificationValidationService,
    private val businessRulesService: VerificationBusinessRulesService,
    private val confirmEventVerificationPort: ConfirmEventVerificationPort,
    private val confirmVerificationService: ConfirmVerificationService
) {
    fun confirm(command: ConfirmVerificationCommand): Mono<ConfirmVerificationResult> {
        return validationService.validate(command)
            .flatMap { cmd -> businessRulesService.applyRules(cmd) }
            .flatMap { cmd -> confirmVerificationService.confirm(cmd) }
            .flatMap { verification -> confirmEventVerificationPort.send(verification) }
            .map { verification -> convertToResult(verification) }
    }

    private fun convertToResult(verification: Verification): ConfirmVerificationResult {
        return ConfirmVerificationResult(true)
    }

}
