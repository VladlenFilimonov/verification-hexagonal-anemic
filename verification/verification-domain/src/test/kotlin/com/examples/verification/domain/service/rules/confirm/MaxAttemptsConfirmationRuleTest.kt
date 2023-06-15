package com.examples.verification.domain.service.rules.confirm

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.config.ApplicationProperties
import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.UserInfo
import com.examples.verification.domain.model.VerificationAttempt
import com.examples.verification.domain.model.VerificationAttempts
import com.examples.verification.domain.port.outbound.ReadVerificationAttemptsPort
import java.time.OffsetDateTime
import java.util.UUID
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class MaxAttemptsConfirmationRuleTest {

    @Mock
    private lateinit var applicationProperties: ApplicationProperties

    @Mock
    private lateinit var readVerificationAttemptsPort: ReadVerificationAttemptsPort

    @InjectMocks
    private lateinit var rule: MaxAttemptsConfirmationRule

    private val VERIFICATION_ID = UUID.fromString("fa7d3fcf-98f1-4dcd-9ed1-3a3ab3c8c943")
    private val CONFIRM_COMMAND = ConfirmVerificationCommand(VERIFICATION_ID, "1234", UserInfo("127.0.0.1", "chrome"))

    @Test
    fun `apply should return command when attempts are within the limit`() {

        val verificationAttempts = VerificationAttempts(emptyList())

        Mockito.`when`(readVerificationAttemptsPort.read(CONFIRM_COMMAND.id))
            .thenReturn(Mono.just(verificationAttempts))
        Mockito.`when`(applicationProperties.verificationMaxAttempts)
            .thenReturn(5)

        StepVerifier.create(rule.apply(CONFIRM_COMMAND))
            .expectNext(CONFIRM_COMMAND)
            .verifyComplete()
    }

    @Test
    fun `apply should throw error when attempts exceed the limit`() {

        val verificationAttempts = VerificationAttempts(
            listOf(
                VerificationAttempt(VERIFICATION_ID, OffsetDateTime.MAX),
                VerificationAttempt(VERIFICATION_ID, OffsetDateTime.MAX),
                VerificationAttempt(VERIFICATION_ID, OffsetDateTime.MAX)
            )
        )

        Mockito.`when`(readVerificationAttemptsPort.read(CONFIRM_COMMAND.id))
            .thenReturn(Mono.just(verificationAttempts))
        Mockito.`when`(applicationProperties.verificationMaxAttempts)
            .thenReturn(2)

        StepVerifier.create(rule.apply(CONFIRM_COMMAND))
            .expectErrorMatches { it is VerificationError && it.errorCode == ErrorCode.VERIFICATION_EXPIRED_BY_MAX_ATTEMPTS }
            .verify()

    }

    @Test
    fun `apply should throw error when verification is not found`() {

        Mockito.`when`(readVerificationAttemptsPort.read(CONFIRM_COMMAND.id))
            .thenReturn(Mono.empty())

        StepVerifier.create(rule.apply(CONFIRM_COMMAND))
            .expectErrorMatches { it is VerificationError && it.errorCode == ErrorCode.VERIFICATION_NOT_FOUND }
            .verify()
    }

}