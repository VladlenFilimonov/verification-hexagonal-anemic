package com.examples.verification.domain.validation

import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.UserInfo
import org.springframework.stereotype.Service

@Service
class UserInfoValidator {

    fun validate(userInfo: UserInfo) {
        if (userInfo.userAgent.isBlank()) {
            throw VerificationError("User agent can't be blank", ErrorCode.INVALID_USER_INFO)
        }
        if (userInfo.userAgent.length > 100) {
            throw VerificationError("User agent is too long", ErrorCode.INVALID_USER_INFO)
        }
        if (userInfo.ipAddress.isBlank()) {
            throw VerificationError("Ip address can't be blank", ErrorCode.INVALID_USER_INFO)
        }
        if (isInvalidIpAddressFormat(userInfo.ipAddress)) {
            throw VerificationError("Invalid ip address format", ErrorCode.INVALID_USER_INFO)
        }
    }

    private fun isInvalidIpAddressFormat(ipAddress: String): Boolean {
        val regex = Regex("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\$")
        return !regex.matches(ipAddress)
    }
}