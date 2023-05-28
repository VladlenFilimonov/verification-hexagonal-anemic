package com.examples.verification.domain.service.rules.confirm

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.UserInfo
import com.examples.verification.domain.port.outbound.AcquireDistributedLockPort
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
class IdempotentConfirmationRuleTest {

    private val VERIFICATION_ID = UUID.fromString("fa7d3fcf-98f1-4dcd-9ed1-3a3ab3c8c943")
    private val CONFIRM_COMMAND = ConfirmVerificationCommand(VERIFICATION_ID, "1234", UserInfo("127.0.0.1", "chrome"))

    @Mock
    private lateinit var acquireDistributedLockPort: AcquireDistributedLockPort

    @InjectMocks
    private lateinit var rule: IdempotentConfirmationRule

    @Test
    fun `apply should acquire lock and return command when lock is acquired`() {

        Mockito.`when`(acquireDistributedLockPort.acquire(CONFIRM_COMMAND))
            .thenReturn(Mono.just(CONFIRM_COMMAND))

        StepVerifier.create(rule.apply(CONFIRM_COMMAND))
            .expectNext(CONFIRM_COMMAND)
            .verifyComplete()

    }

    @Test
    fun `apply should return error when lock is not acquired`() {

        Mockito.`when`(acquireDistributedLockPort.acquire(CONFIRM_COMMAND))
            .thenReturn(Mono.empty())

        StepVerifier.create(rule.apply(CONFIRM_COMMAND))
            .expectErrorMatches { it is VerificationError && it.errorCode == ErrorCode.VERIFICATION_IDEMPOTENT_RULE_VIOLATION }
            .verify()
    }

    @Test
    fun `apply should return error when lock acquisition fails`() {
        val error = RuntimeException("Lock acquisition failed")

        Mockito.`when`(acquireDistributedLockPort.acquire(CONFIRM_COMMAND))
            .thenReturn(Mono.error(error))

        StepVerifier.create(rule.apply(CONFIRM_COMMAND))
            .expectErrorMatches { it is VerificationError && it.errorCode == ErrorCode.VERIFICATION_IDEMPOTENT_RULE_VIOLATION && it.cause == error }
            .verify()
    }
}