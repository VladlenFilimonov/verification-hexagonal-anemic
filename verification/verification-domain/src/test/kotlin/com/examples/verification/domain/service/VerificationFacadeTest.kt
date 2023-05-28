package com.examples.verification.domain.service

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.ConfirmVerificationResult
import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.api.CreateVerificationResult
import com.examples.verification.domain.port.outbound.ErrorEventVerificationPort
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
class VerificationFacadeTest {

    @Mock
    private lateinit var createVerificationFacade: CreateVerificationFacade

    @Mock
    private lateinit var confirmVerificationFacade: ConfirmVerificationFacade

    @Mock
    private lateinit var errorEventVerificationPort: ErrorEventVerificationPort

    @InjectMocks
    private lateinit var facade: VerificationFacade

    @Test
    fun `confirm should return ConfirmVerificationResult`() {

        val command = mock(ConfirmVerificationCommand::class.java)
        val verificationResult = mock(ConfirmVerificationResult::class.java)

        Mockito.`when`(confirmVerificationFacade.confirm(command))
            .thenReturn(Mono.just(verificationResult))

        StepVerifier.create(facade.confirm(command))
            .expectNext(verificationResult)
            .verifyComplete()

        verify(confirmVerificationFacade).confirm(command)

        verifyNoMoreInteractions(confirmVerificationFacade)

    }

    @Test
    fun `confirm should handle error and send error event`() {

        val command = mock(ConfirmVerificationCommand::class.java)
        val error = RuntimeException("Some error")

        Mockito.`when`(confirmVerificationFacade.confirm(command))
            .thenReturn(Mono.error(error))

        StepVerifier.create(facade.confirm(command))
            .verifyError()

        verify(confirmVerificationFacade).confirm(command)
        verify(errorEventVerificationPort).send(error, command)

        verifyNoMoreInteractions(confirmVerificationFacade, errorEventVerificationPort)

    }

    @Test
    fun `create should return CreateVerificationResult`() {

        val command = mock(CreateVerificationCommand::class.java)
        val verificationResult = mock(CreateVerificationResult::class.java)

        Mockito.`when`(createVerificationFacade.create(command))
            .thenReturn(Mono.just(verificationResult))

        StepVerifier.create(facade.create(command))
            .expectNext(verificationResult)
            .verifyComplete()

        verify(createVerificationFacade).create(command)

        verifyNoMoreInteractions(createVerificationFacade)

    }

    @Test
    fun `create should handle error and send error event`() {

        val command = mock(CreateVerificationCommand::class.java)
        val error = RuntimeException("Some error")

        Mockito.`when`(createVerificationFacade.create(command))
            .thenReturn(Mono.error(error))

        StepVerifier.create(facade.create(command))
            .verifyError()

        verify(createVerificationFacade).create(command)
        verify(errorEventVerificationPort).send(error, command)

        verifyNoMoreInteractions(createVerificationFacade, errorEventVerificationPort)
    }

}