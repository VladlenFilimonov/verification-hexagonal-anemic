package com.examples.verification.domain.error

enum class ErrorCode(val code: Int) {
    INVALID_SUBJECT(1),
    INVALID_USER_INFO(2),
    VERIFICATION_EXPIRED(3),
    VERIFICATION_NOT_FOUND(4)

}