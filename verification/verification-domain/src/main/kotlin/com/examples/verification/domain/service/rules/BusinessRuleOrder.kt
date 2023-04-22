package com.examples.verification.domain.service.rules

enum class BusinessRuleOrder(val order: Int) {
    VERIFICATION_EXPIRATION_RULE(1),
    VERIFICATION_DUPLICATION_RULE(2),
    USER_INFO_MATCHING_RULE(3)
}