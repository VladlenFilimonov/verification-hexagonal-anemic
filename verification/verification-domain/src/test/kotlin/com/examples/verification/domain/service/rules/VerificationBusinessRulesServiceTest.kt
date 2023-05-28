package com.examples.verification.domain.service.rules

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.SubjectType
import com.examples.verification.domain.model.UserInfo
import com.examples.verification.domain.service.rules.confirm.IdempotentConfirmationRule
import com.examples.verification.domain.service.rules.confirm.MaxAttemptsConfirmationRule
import com.examples.verification.domain.service.rules.confirm.UserInfoMatchingRule
import com.examples.verification.domain.service.rules.confirm.VerificationExpirationRule
import com.examples.verification.domain.service.rules.create.DuplicationVerificationRule
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
class VerificationBusinessRulesServiceTest {

    @Mock
    private lateinit var duplicationVerificationRule: DuplicationVerificationRule

    @Mock
    private lateinit var userInfoMatchingRule: UserInfoMatchingRule

    @Mock
    private lateinit var verificationExpirationRule: VerificationExpirationRule

    @Mock
    private lateinit var maxAttemptsConfirmationRule: MaxAttemptsConfirmationRule

    @Mock
    private lateinit var idempotentConfirmationRule: IdempotentConfirmationRule

    @InjectMocks
    private lateinit var rulesService: VerificationBusinessRulesService

    private val CREATE_COMMAND = CreateVerificationCommand(
        Subject("john@doe.com", SubjectType.EMAIL_CONFIRMATION),
        UserInfo("127.0.0.1", "User Agent")
    )
    private val VERIFICATION_ID = UUID.fromString("fa7d3fcf-98f1-4dcd-9ed1-3a3ab3c8c943")
    private val CONFIRM_COMMAND =
        ConfirmVerificationCommand(VERIFICATION_ID, "1234", UserInfo("127.0.0.1", "User Agent"))

    @Test
    fun `applyRules with CreateVerificationCommand should return command`() {
        Mockito.`when`(duplicationVerificationRule.apply(CREATE_COMMAND))
            .thenReturn(Mono.just(CREATE_COMMAND))

        StepVerifier.create(rulesService.applyRules(CREATE_COMMAND))
            .expectNext(CREATE_COMMAND)
            .verifyComplete()
    }

    @Test
    fun `applyRules with ConfirmVerificationCommand should return command`() {

        Mockito.`when`(idempotentConfirmationRule.apply(CONFIRM_COMMAND))
            .thenReturn(Mono.just(CONFIRM_COMMAND))
        Mockito.`when`(userInfoMatchingRule.apply(CONFIRM_COMMAND))
            .thenReturn(Mono.just(CONFIRM_COMMAND))
        Mockito.`when`(verificationExpirationRule.apply(CONFIRM_COMMAND))
            .thenReturn(Mono.just(CONFIRM_COMMAND))
        Mockito.`when`(maxAttemptsConfirmationRule.apply(CONFIRM_COMMAND))
            .thenReturn(Mono.just(CONFIRM_COMMAND))

        StepVerifier.create(rulesService.applyRules(CONFIRM_COMMAND))
            .expectNext(CONFIRM_COMMAND)
            .verifyComplete()
    }

}