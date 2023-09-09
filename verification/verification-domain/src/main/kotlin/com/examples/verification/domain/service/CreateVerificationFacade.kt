package com.examples.verification.domain.service

import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.api.CreateVerificationResult
import com.examples.verification.domain.port.outbound.CreateVerificationEventPort
import com.examples.verification.domain.port.outbound.CreateVerificationPort
import com.examples.verification.domain.service.rules.VerificationBusinessRulesService
import com.examples.verification.domain.validation.VerificationValidationService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreateVerificationFacade(
    private val validationService: VerificationValidationService,
    private val verificationBusinessRulesService: VerificationBusinessRulesService,
    private val verificationFactory: VerificationFactory,
    private val createVerificationPort: CreateVerificationPort,
    private val createVerificationEventPort: CreateVerificationEventPort
) {
    fun create(command: CreateVerificationCommand): Mono<CreateVerificationResult> {
        return validationService.validate(command)
            .flatMap(verificationBusinessRulesService::applyRules)
            .flatMap(verificationFactory::buildVerification)
            .flatMap(createVerificationPort::create)
            .flatMap(createVerificationEventPort::send)
            .map { CreateVerificationResult(it.id) }
    }
}
