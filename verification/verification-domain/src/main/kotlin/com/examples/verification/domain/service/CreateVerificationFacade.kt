package com.examples.verification.domain.service

import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.api.CreateVerificationResult
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
            .flatMap(verificationBusinessRulesService::applyRules)
            .flatMap(verificationFactory::buildVerification)
            .flatMap(saveVerificationPort::save)
            .flatMap(createEventVerificationPort::send)
            .map { CreateVerificationResult(it.id) }
    }
}
