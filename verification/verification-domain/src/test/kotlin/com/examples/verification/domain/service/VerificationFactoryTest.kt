package com.examples.verification.domain.service

import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.config.ApplicationProperties
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.SubjectType
import com.examples.verification.domain.model.UserInfo
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.service.generator.VerificationCodeGenerator
import com.examples.verification.domain.service.generator.VerificationIdGenerator
import java.util.UUID
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.jupiter.MockitoExtension
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class VerificationFactoryTest {

    @Mock
    private lateinit var applicationProperties: ApplicationProperties

    @Mock
    private lateinit var codeGenerator: VerificationCodeGenerator

    @Mock
    private lateinit var idGenerator: VerificationIdGenerator

    @InjectMocks
    private lateinit var factory: VerificationFactory

    @Test
    fun `buildVerification should return a Verification object`() {
        val command = CreateVerificationCommand(
            Subject("john@doe.com", SubjectType.EMAIL_CONFIRMATION),
            UserInfo("127.0.0.1", "User Agent")
        )
        val verificationCode = "123456"
        val id = UUID.randomUUID()
        val expectedVerification = Verification(
            id = id,
            confirmed = false,
            expired = false,
            code = verificationCode,
            subject = command.subject,
            userInfo = command.userInfo
        )

        Mockito.`when`(applicationProperties.verificationCodeLength)
            .thenReturn(6)
        Mockito.`when`(codeGenerator.generate(6))
            .thenReturn(verificationCode)
        Mockito.`when`(idGenerator.generate())
            .thenReturn(id)

        StepVerifier.create(factory.buildVerification(command))
            .expectNext(expectedVerification)
            .verifyComplete()

        verify(applicationProperties).verificationCodeLength
        verify(codeGenerator).generate(6)

        verifyNoMoreInteractions(applicationProperties, codeGenerator)
    }
}