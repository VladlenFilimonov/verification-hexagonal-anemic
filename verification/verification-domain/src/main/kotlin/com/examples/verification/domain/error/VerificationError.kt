package com.examples.verification.domain.error

class VerificationError(
    message: String,
    val errorCode: ErrorCode,
    throwable: Throwable?
) : RuntimeException(message, throwable) {

    constructor(message: String, errorCode: ErrorCode) : this(message, errorCode, null)

}
