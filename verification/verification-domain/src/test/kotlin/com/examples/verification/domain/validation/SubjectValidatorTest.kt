package com.examples.verification.domain.validation

import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.SubjectType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class SubjectValidatorTest {

    private val validator = SubjectValidator()

    @Test
    fun `validate should throw VerificationError for blank identity`() {

        val subject = Subject("", SubjectType.EMAIL_CONFIRMATION)

        val exception = assertThrows(VerificationError::class.java) {
            validator.validate(subject)
        }
        assertEquals(ErrorCode.INVALID_SUBJECT, exception.errorCode)
    }

    @Test
    fun `validate should throw VerificationError for identity that is too long`() {

        val subject = Subject("a".repeat(101), SubjectType.EMAIL_CONFIRMATION)

        val exception = assertThrows(VerificationError::class.java) {
            validator.validate(subject)
        }

        assertEquals(ErrorCode.INVALID_SUBJECT, exception.errorCode)
    }

    @Test
    fun `validate should throw VerificationError for invalid email format`() {

        val subject = Subject("invalid_email", SubjectType.EMAIL_CONFIRMATION)

        val exception = assertThrows(VerificationError::class.java) {
            validator.validate(subject)
        }

        assertEquals(ErrorCode.INVALID_SUBJECT, exception.errorCode)
    }

    @Test
    fun `validate should throw VerificationError for invalid phone number format`() {

        val subject = Subject("invalid_phone_number", SubjectType.MOBILE_CONFIRMATION)

        val exception = assertThrows(VerificationError::class.java) {
            validator.validate(subject)
        }

        assertEquals(ErrorCode.INVALID_SUBJECT, exception.errorCode)
    }

    @Test
    fun `validate should not throw VerificationError for valid email subject`() {

        val subject = Subject("john@doe.com", SubjectType.EMAIL_CONFIRMATION)

        validator.validate(subject) // No exception should be thrown
    }

    @Test
    fun `validate should not throw VerificationError for valid phone number subject`() {

        val subject = Subject("+37112345678", SubjectType.MOBILE_CONFIRMATION)

        validator.validate(subject) // No exception should be thrown
    }
}