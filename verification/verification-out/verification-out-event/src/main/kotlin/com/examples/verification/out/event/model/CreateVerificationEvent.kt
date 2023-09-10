package com.examples.verification.out.event.model

import com.examples.verification.domain.model.Subject

data class CreateVerificationEvent(
    val id: String,
    val code: String,
    val subject: Subject,
    val occurredOn: String
)