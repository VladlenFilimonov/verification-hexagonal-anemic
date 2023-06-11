package com.examples.verification.domain.model

import java.util.UUID

data class Verification(
    val id: UUID?,
    val confirmed: Boolean = false,
    val expired: Boolean,
    val code: String?,
    val subject: Subject,
    val userInfo: UserInfo
)

data class Subject(
    val identity: String,
    val type: SubjectType
)

data class UserInfo(
    val ipAddress: String,
    val userAgent: String
)

enum class SubjectType {
    MOBILE_CONFIRMATION,
    EMAIL_CONFIRMATION
}