package com.examples.verification.domain.service.rules.confirm

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.SubjectType
import com.examples.verification.domain.model.UserInfo
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.ReadVerificationPort
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
class VerificationExpirationRuleTest {

    @Mock
    private lateinit var readVerificationPort: ReadVerificationPort

    @InjectMocks
    private lateinit var rule: VerificationExpirationRule

    private val VERIFICATION_ID = UUID.fromString("fa7d3fcf-98f1-4dcd-9ed1-3a3ab3c8c943")
    private val CONFIRM_COMMAND =
        ConfirmVerificationCommand(VERIFICATION_ID, "1234", UserInfo("127.0.0.1", "User Agent"))

    @Test
    fun `apply should return command when verification is not expired`() {
        val verification = Verification(
            id = VERIFICATION_ID,
            confirmed = false,
            expired = false,
            code = "1234",
            Subject("john@doe.com", SubjectType.EMAIL_CONFIRMATION),
            UserInfo("127.0.0.1", "User Agent")
        )

        Mockito.`when`(readVerificationPort.read(VERIFICATION_ID))
            .thenReturn(Mono.just(verification))

        StepVerifier.create(rule.apply(CONFIRM_COMMAND))
            .expectNext(CONFIRM_COMMAND)
            .verifyComplete()
    }

    @Test
    fun `apply should throw error when verification is expired`() {
        val verification = Verification(
            id = VERIFICATION_ID,
            confirmed = false,
            expired = true,
            code = "1234",
            Subject("john@doe.com", SubjectType.EMAIL_CONFIRMATION),
            UserInfo("127.0.0.1", "User Agent")
        )

        Mockito.`when`(readVerificationPort.read(VERIFICATION_ID))
            .thenReturn(Mono.just(verification))

        StepVerifier.create(rule.apply(CONFIRM_COMMAND))
            .expectErrorMatches { it is VerificationError && it.errorCode == ErrorCode.VERIFICATION_EXPIRED }
            .verify()
    }

    @Test
    fun `apply should throw error when verification is not found`() {

        Mockito.`when`(readVerificationPort.read(VERIFICATION_ID))
            .thenReturn(Mono.empty())

        StepVerifier.create(rule.apply(CONFIRM_COMMAND))
            .expectErrorMatches { it is VerificationError && it.errorCode == ErrorCode.VERIFICATION_NOT_FOUND }
            .verify()
    }
}