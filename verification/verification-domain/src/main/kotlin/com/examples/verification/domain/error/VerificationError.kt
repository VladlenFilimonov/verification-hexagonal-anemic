package com.examples.verification.domain.error

class VerificationError(
    message: String,
    val errorCode: ErrorCode
) : RuntimeException(message) {
}
