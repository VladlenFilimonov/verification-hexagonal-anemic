package com.examples.verification.domain.service

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.SubjectType
import com.examples.verification.domain.model.UserInfo
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.ReadVerificationPort
import com.examples.verification.domain.port.outbound.SaveVerificationAttemptsPort
import com.examples.verification.domain.port.outbound.UpdateVerificationPort
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
class ConfirmVerificationServiceTest {

    @Mock
    private lateinit var readVerificationPort: ReadVerificationPort

    @Mock
    private lateinit var updateVerificationPort: UpdateVerificationPort

    @Mock
    private lateinit var saveVerificationAttemptsPort: SaveVerificationAttemptsPort

    @InjectMocks
    private lateinit var confirmVerificationService: ConfirmVerificationService

    private val VERIFICATION_ID = UUID.fromString("fa7d3fcf-98f1-4dcd-9ed1-3a3ab3c8c943")
    private val CODE = "1234"
    private val USER_INFO = UserInfo("127.0.0.1", "Chrome")
    private val SUBJECT = Subject("JohnDoe@email.com", SubjectType.EMAIL_CONFIRMATION);

    @Test
    fun `confirm should set verification to confirmed and save it when code matches`() {
        val cmd = ConfirmVerificationCommand(VERIFICATION_ID, CODE, USER_INFO)
        val verification = Verification(VERIFICATION_ID, false, false, CODE, SUBJECT, USER_INFO)
        val expected = Verification(VERIFICATION_ID, true, false, CODE, SUBJECT, USER_INFO)

        Mockito.`when`(readVerificationPort.read(cmd.id))
            .thenReturn(Mono.just(verification))
        Mockito.`when`(updateVerificationPort.update(expected))
            .thenReturn(Mono.just(expected))

        StepVerifier.create(confirmVerificationService.confirm(cmd))
            .expectNext(expected)
            .verifyComplete()
    }

    @Test
    fun `confirm should throw VerificationError when code doesn't match`() {
        val cmd = ConfirmVerificationCommand(VERIFICATION_ID, CODE, USER_INFO)
        val verification = Verification(VERIFICATION_ID, false, false, "5678", SUBJECT, USER_INFO)

        Mockito.`when`(readVerificationPort.read(cmd.id))
            .thenReturn(Mono.just(verification))
        Mockito.`when`(saveVerificationAttemptsPort.save(verification))
            .thenReturn(Mono.just(verification))

        StepVerifier.create(confirmVerificationService.confirm(cmd))
            .expectErrorMatches { error -> assertCode(error, ErrorCode.VERIFICATION_CODE_NOT_MATCH) }
            .verify()
    }

    @Test
    fun `confirm should throw VerificationError when verification not found`() {
        val cmd = ConfirmVerificationCommand(VERIFICATION_ID, CODE, USER_INFO)

        Mockito.`when`(readVerificationPort.read(cmd.id))
            .thenReturn(Mono.empty())

        StepVerifier.create(confirmVerificationService.confirm(cmd))
            .expectErrorMatches { error -> assertCode(error, ErrorCode.VERIFICATION_NOT_FOUND) }
            .verify()
    }

    private fun assertCode(error: Throwable, code: ErrorCode): Boolean {
        return if (error is VerificationError) {
            error.errorCode == code
        } else {
            false
        }
    }
}