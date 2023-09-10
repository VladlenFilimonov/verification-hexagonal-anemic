package com.examples.verification.domain.service

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.ConfirmVerificationResult
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.ConfirmVerificationEventPort
import com.examples.verification.domain.service.rules.VerificationBusinessRulesService
import com.examples.verification.domain.validation.VerificationValidationService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class ConfirmVerificationFacadeTest {

    @Mock
    private lateinit var validationService: VerificationValidationService

    @Mock
    private lateinit var businessRulesService: VerificationBusinessRulesService

    @Mock
    private lateinit var confirmVerificationEventPort: ConfirmVerificationEventPort

    @Mock
    private lateinit var confirmVerificationService: ConfirmVerificationService

    @InjectMocks
    private lateinit var confirmVerificationFacade: ConfirmVerificationFacade

    private val command = mock(ConfirmVerificationCommand::class.java)
    private val verification = mock(Verification::class.java)

    @Test
    fun `confirm returns ConfirmVerificationResult when validation, business rules, and confirmation succeed`() {
        val expected = ConfirmVerificationResult(true)

        Mockito.`when`(validationService.validate(command))
            .thenReturn(Mono.just(command))
        Mockito.`when`(businessRulesService.applyRules(command))
            .thenReturn(Mono.just(command))
        Mockito.`when`(confirmVerificationService.confirm(command))
            .thenReturn(Mono.just(verification))
        Mockito.`when`(confirmVerificationEventPort.sendConfirm(verification))
            .thenReturn(Mono.just(verification))

        StepVerifier.create(confirmVerificationFacade.confirm(command))
            .expectNext(expected)
            .verifyComplete()
    }

    @Test
    fun `confirm returns an error when validation fails`() {
        val validationError = RuntimeException("Validation failed")

        Mockito.`when`(validationService.validate(command)).thenReturn(Mono.error(validationError))

        StepVerifier.create(confirmVerificationFacade.confirm(command))
            .expectError(validationError::class.java)
            .verify()
    }

    @Test
    fun `confirm returns an error when business rules fail`() {
        val businessRulesError = RuntimeException("Business rules failed")

        Mockito.`when`(validationService.validate(command)).thenReturn(Mono.just(command))
        Mockito.`when`(businessRulesService.applyRules(command)).thenReturn(Mono.error(businessRulesError))

        StepVerifier.create(confirmVerificationFacade.confirm(command))
            .expectError(businessRulesError::class.java)
            .verify()
    }

    @Test
    fun `confirm returns an error when confirmation fails`() {
        val verificationError = RuntimeException("Confirmation failed")

        Mockito.`when`(validationService.validate(command)).thenReturn(Mono.just(command))
        Mockito.`when`(businessRulesService.applyRules(command)).thenReturn(Mono.just(command))
        Mockito.`when`(confirmVerificationService.confirm(command)).thenReturn(Mono.error(verificationError))

        StepVerifier.create(confirmVerificationFacade.confirm(command))
            .expectError(verificationError::class.java)
            .verify()
    }

    @Test
    fun `confirm returns an error when event verification fails`() {
        val eventVerificationError = RuntimeException("Event verification failed")

        Mockito.`when`(validationService.validate(command)).thenReturn(Mono.just(command))
        Mockito.`when`(businessRulesService.applyRules(command)).thenReturn(Mono.just(command))
        Mockito.`when`(confirmVerificationService.confirm(command)).thenReturn(Mono.just(verification))
        Mockito.`when`(confirmVerificationEventPort.sendConfirm(verification))
            .thenReturn(Mono.error(eventVerificationError))

        StepVerifier.create(confirmVerificationFacade.confirm(command))
            .expectError(eventVerificationError::class.java)
            .verify()
    }
}