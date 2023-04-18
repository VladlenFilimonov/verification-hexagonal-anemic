package com.examples.verification.domain.validation

import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.SubjectType
import org.springframework.stereotype.Service

@Service
class SubjectValidator {

    fun validate(subject: Subject) {
        if (subject.identity.isBlank()) {
            throw VerificationError("Subject identity can't be blank", ErrorCode.INVALID_SUBJECT)
        }

        if (subject.identity.length > 100) {
            throw VerificationError("Subject identity is too long", ErrorCode.INVALID_SUBJECT)
        }

        if (subject.type == SubjectType.EMAIL_CONFIRMATION && isEmailFormatInvalid(subject.identity)) {
            throw VerificationError("Invalid email format", ErrorCode.INVALID_SUBJECT)
        }

        if (subject.type == SubjectType.MOBILE_CONFIRMATION && isPhoneNumberFormatInvalid(subject.identity)) {
            throw VerificationError("Invalid phone number format", ErrorCode.INVALID_SUBJECT)
        }
    }

    private fun isPhoneNumberFormatInvalid(phoneNumber: String): Boolean {
        val regex = Regex("^\\+(?:[0-9] ?){6,14}[0-9]\$")
        return !regex.matches(phoneNumber)
    }

    private fun isEmailFormatInvalid(email: String): Boolean {
        val regex = Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        return !regex.matches(email)
    }
}