package com.examples.verification.domain.error

enum class ErrorCode(val code: Int) {
    INVALID_SUBJECT(1),
    INVALID_USER_INFO(2),
    VERIFICATION_EXPIRED(3),
    VERIFICATION_NOT_FOUND(4),
    VERIFICATION_DUPLICATION(5),
    USER_AGENT_NOT_MATCH(6),
    IP_ADDRESS_NOT_MATCH(7),
    VERIFICATION_EXPIRED_BY_MAX_ATTEMPTS(8),
    VERIFICATION_IDEMPOTENT_RULE_VIOLATION(9),
    VERIFICATION_CODE_NOT_MATCH(10)

}