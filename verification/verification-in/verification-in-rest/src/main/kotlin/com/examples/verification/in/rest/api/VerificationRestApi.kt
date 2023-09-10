package com.examples.verification.`in`.rest.api

data class CreateVerificationRequest(
    val subject: Subject,
    val userInfo: UserInfo?
)

data class CreateVerificationResponse(
    val id: String
)

data class ConfirmVerificationRequest(
    val verificationId: String?,
    val code: String,
    val userInfo: UserInfo?
)

data class Subject(
    val identity: String,
    val type: String
)

data class UserInfo(
    val ipAddress: String,
    val userAgent: String
)

data class RestError(
    val errorCode: String,
    val errorMessage: String?
)