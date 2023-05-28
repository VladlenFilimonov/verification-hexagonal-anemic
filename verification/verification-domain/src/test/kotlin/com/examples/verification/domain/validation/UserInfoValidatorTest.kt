package com.examples.verification.domain.validation

import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.UserInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class UserInfoValidatorTest {

    private val validator = UserInfoValidator()

    @Test
    fun `validate should throw VerificationError for blank user agent`() {

        val userInfo = UserInfo("192.168.0.1", "")

        val exception = assertThrows(VerificationError::class.java) {
            validator.validate(userInfo)
        }

        assertEquals(ErrorCode.INVALID_USER_INFO, exception.errorCode)

    }

    @Test
    fun `validate should throw VerificationError for user agent that is too long`() {

        val userInfo = UserInfo("192.168.0.1", "a".repeat(101))

        val exception = assertThrows(VerificationError::class.java) {
            validator.validate(userInfo)
        }

        assertEquals(ErrorCode.INVALID_USER_INFO, exception.errorCode)

    }

    @Test
    fun `validate should throw VerificationError for blank IP address`() {

        val userInfo = UserInfo("", "UserAgent")

        val exception = assertThrows(VerificationError::class.java) {
            validator.validate(userInfo)
        }

        assertEquals(ErrorCode.INVALID_USER_INFO, exception.errorCode)

    }

    @Test
    fun `validate should throw VerificationError for invalid IP address format`() {

        val userInfo = UserInfo("invalid_ip_address", "UserAgent")

        val exception = assertThrows(VerificationError::class.java) {
            validator.validate(userInfo)
        }

        assertEquals(ErrorCode.INVALID_USER_INFO, exception.errorCode)

    }

    @Test
    fun `validate should not throw VerificationError for valid user info`() {

        val userInfo = UserInfo("192.168.0.1", "UserAgent")

        validator.validate(userInfo) // No exception should be thrown

    }
}