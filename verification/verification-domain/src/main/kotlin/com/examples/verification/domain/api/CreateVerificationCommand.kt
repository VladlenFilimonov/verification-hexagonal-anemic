package com.examples.verification.domain.api

import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.UserInfo

data class CreateVerificationCommand(
    val subject: Subject,
    val userInfo: UserInfo
)
