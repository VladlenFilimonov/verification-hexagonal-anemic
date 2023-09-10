package com.examples.verification.out.event.model

import com.examples.verification.domain.model.Subject

data class CreateVerificationEvent(
    val id: String,
    val code: String?,
    val subject: Subject,
    val occurredOn: OccurredOn
)

data class ConfirmVerificationEvent(
    val id: String,
    val code: String?,
    val subject: Subject,
    val occurredOn: OccurredOn
)

enum class OccurredOn {
    CREATE_VERIFICATION,
    CREATE_VERIFICATION_ERROR,
    CONFIRM_VERIFICATION,
    CONFIRM_VERIFICATION_ERROR,
}

enum class VerificationOutTopic {
    CREATE_VERIFICATION_TOPIC,
    CONFIRM_VERIFICATION_TOPIC
}