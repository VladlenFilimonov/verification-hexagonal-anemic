package com.examples.verification.domain.api

import com.examples.verification.domain.model.UserInfo

data class ConfirmVerificationCommand(
    val code: Long,
    val userInfo: UserInfo
)
