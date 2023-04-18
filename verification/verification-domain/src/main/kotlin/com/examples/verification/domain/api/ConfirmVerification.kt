package com.examples.verification.domain.api

import com.examples.verification.domain.model.UserInfo
import java.util.UUID

data class ConfirmVerificationCommand(
    val id: UUID,
    val code: Long,
    val userInfo: UserInfo
)

data class ConfirmVerificationResult(
    val success: Boolean
)
