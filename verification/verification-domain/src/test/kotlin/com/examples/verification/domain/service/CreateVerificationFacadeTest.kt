package com.examples.verification.domain.service

import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.api.CreateVerificationResult
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.CreateEventVerificationPort
import com.examples.verification.domain.port.outbound.CreateVerificationPort
import com.examples.verification.domain.service.rules.VerificationBusinessRulesService
import com.examples.verification.domain.validation.VerificationValidationService
import java.util.UUID
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class CreateVerificationFacadeTest {

    @Mock
    private lateinit var validationService: VerificationValidationService

    @Mock
    private lateinit var verificationBusinessRulesService: VerificationBusinessRulesService

    @Mock
    private lateinit var verificationFactory: VerificationFactory

    @Mock
    private lateinit var createVerificationPort: CreateVerificationPort

    @Mock
    private lateinit var createEventVerificationPort: CreateEventVerificationPort

    @InjectMocks
    private lateinit var facade: CreateVerificationFacade

    @Test
    fun `create should return CreateVerificationResult`() {
        val verificationId = UUID.randomUUID();
        val command = mock(CreateVerificationCommand::class.java)
        val verification = mock(Verification::class.java)

        Mockito.`when`(validationService.validate(command))
            .thenReturn(Mono.just(command))
        Mockito.`when`(verificationBusinessRulesService.applyRules(command))
            .thenReturn(Mono.just(command))
        Mockito.`when`(verificationFactory.buildVerification(command))
            .thenReturn(Mono.just(verification))
        Mockito.`when`(createVerificationPort.create(verification))
            .thenReturn(Mono.just(verification))
        Mockito.`when`(createEventVerificationPort.send(verification))
            .thenReturn(Mono.just(verification))
        Mockito.`when`(verification.id).thenReturn(verificationId)

        StepVerifier.create(facade.create(command))
            .expectNext(CreateVerificationResult(verificationId))
            .verifyComplete()

        verify(validationService).validate(command)
        verify(verificationBusinessRulesService).applyRules(command)
        verify(verificationFactory).buildVerification(command)
        verify(createVerificationPort).create(verification)
        verify(createEventVerificationPort).send(verification)

        verifyNoMoreInteractions(
            validationService, verificationBusinessRulesService, verificationFactory,
            createVerificationPort, createEventVerificationPort
        )
    }
}