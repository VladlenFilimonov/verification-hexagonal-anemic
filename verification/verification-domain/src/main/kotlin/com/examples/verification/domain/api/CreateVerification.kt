package com.examples.verification.domain.api

import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.UserInfo
import java.util.UUID

data class CreateVerificationCommand(
    val subject: Subject,
    val userInfo: UserInfo
)

data class CreateVerificationResult(
    val id: UUID?
)
