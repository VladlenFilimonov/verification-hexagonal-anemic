package com.examples.verification.domain.service

import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.api.CreateVerificationResult
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.CreateEventVerificationPort
import com.examples.verification.domain.port.outbound.SaveVerificationPort
import com.examples.verification.domain.service.rules.VerificationBusinessRulesService
import com.examples.verification.domain.validation.VerificationValidationService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreateVerificationFacade(
    private val validationService: VerificationValidationService,
    private val verificationBusinessRulesService: VerificationBusinessRulesService,
    private val verificationFactory: VerificationFactory,
    private val saveVerificationPort: SaveVerificationPort,
    private val createEventVerificationPort: CreateEventVerificationPort
) {

    fun create(command: CreateVerificationCommand): Mono<CreateVerificationResult> {
        return validationService.validate(command)
            .flatMap { cmd -> verificationBusinessRulesService.applyRules(cmd) }
            .flatMap { cmd -> verificationFactory.buildVerification(cmd) }
            .flatMap { verification -> saveVerificationPort.save(verification) }
            .flatMap { verification -> createEventVerificationPort.send(verification) }
            .map { verification -> convertToResult(verification) }
    }

    private fun convertToResult(verification: Verification): CreateVerificationResult {
        return CreateVerificationResult(verification.id);
    }

}
