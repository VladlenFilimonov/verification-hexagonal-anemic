package com.examples.verification.domain.validation

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.UserInfo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class VerificationValidationServiceTest {

    @Mock
    private lateinit var subjectValidator: SubjectValidator

    @Mock
    private lateinit var userInfoValidator: UserInfoValidator

    @InjectMocks
    private lateinit var validationService: VerificationValidationService

    @Test
    fun `validate should call subjectValidator and userInfoValidator for CreateVerificationCommand`() {

        val command = mock(CreateVerificationCommand::class.java)

        StepVerifier.create(validationService.validate(command))
            .expectNext(command)
            .verifyComplete()

        verify(subjectValidator).validate(command.subject)
        verify(userInfoValidator).validate(command.userInfo)
    }

    @Test
    fun `validate should call userInfoValidator for ConfirmVerificationCommand`() {

        val command = mock(ConfirmVerificationCommand::class.java)

        StepVerifier.create(validationService.validate(command))
            .expectNext(command)
            .verifyComplete()

        verify(userInfoValidator).validate(command.userInfo)

    }

    @Test
    fun `validate should throw an error if subject validation fails for CreateVerificationCommand`() {

        val command = mock(CreateVerificationCommand::class.java)
        val subject = mock(Subject::class.java)
        val error = RuntimeException("Some error")

        Mockito.`when`(command.subject)
            .thenReturn(subject)
        Mockito.`when`(subjectValidator.validate(subject))
            .thenThrow(error)

        StepVerifier.create(validationService.validate(command))
            .expectError(error::class.java)
            .verify()

    }

    @Test
    fun `validate should throw an error if userInfo validation fails for CreateVerificationCommand`() {

        val command = mock(CreateVerificationCommand::class.java)
        val userInfo = mock(UserInfo::class.java)
        val error = RuntimeException("Some error")

        Mockito.`when`(command.userInfo)
            .thenReturn(userInfo)
        Mockito.`when`(userInfoValidator.validate(userInfo))
            .thenThrow(error)

        StepVerifier.create(validationService.validate(command))
            .expectError(error::class.java)
            .verify()

    }

    @Test
    fun `validate should throw an error if userInfo validation fails for ConfirmVerificationCommand`() {

        val command = mock(ConfirmVerificationCommand::class.java)
        val userInfo = mock(UserInfo::class.java)
        val error = RuntimeException("Some error")

        Mockito.`when`(command.userInfo)
            .thenReturn(userInfo)
        Mockito.`when`(userInfoValidator.validate(userInfo))
            .thenThrow(error)

        StepVerifier.create(validationService.validate(command))
            .expectError(error::class.java)
            .verify()

    }
}