package com.examples.verification.domain.service.rules.create

import com.examples.verification.domain.api.CreateVerificationCommand
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
class DuplicationVerificationRuleTest {

    @Mock
    private lateinit var readVerificationPort: ReadVerificationPort

    @InjectMocks
    private lateinit var rule: DuplicationVerificationRule

    private val COMMAND = CreateVerificationCommand(
        Subject("john@doe.com", SubjectType.EMAIL_CONFIRMATION),
        UserInfo("127.0.0.1", "User Agent")
    )

    @Test
    fun `apply should return command when no duplication is found`() {

        Mockito.`when`(readVerificationPort.read(COMMAND.subject))
            .thenReturn(Mono.empty())

        StepVerifier.create(rule.apply(COMMAND))
            .expectNext(COMMAND)
            .verifyComplete()

    }

    @Test
    fun `apply should return command when duplication is found and expired`() {

        val verification = Verification(
            id = UUID.randomUUID(),
            confirmed = false,
            expired = true,
            code = "1234",
            Subject("john@doe.com", SubjectType.EMAIL_CONFIRMATION),
            UserInfo("127.0.0.1", "User Agent")
        )

        Mockito.`when`(readVerificationPort.read(COMMAND.subject))
            .thenReturn(Mono.just(verification))

        StepVerifier.create(rule.apply(COMMAND))
            .expectNext(COMMAND)
            .verifyComplete()

    }

    @Test
    fun `apply should throw error when duplication is found`() {

        val verification = Verification(
            id = UUID.randomUUID(),
            confirmed = false,
            expired = false,
            code = "1234",
            Subject("john@doe.com", SubjectType.EMAIL_CONFIRMATION),
            UserInfo("127.0.0.1", "User Agent")
        )

        Mockito.`when`(readVerificationPort.read(COMMAND.subject))
            .thenReturn(Mono.just(verification))

        StepVerifier.create(rule.apply(COMMAND))
            .expectErrorMatches { it is VerificationError && it.errorCode == ErrorCode.VERIFICATION_DUPLICATION }
            .verify()

    }
}