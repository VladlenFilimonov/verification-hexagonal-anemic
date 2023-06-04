package com.examples.verification.`in`.rest.api

data class CreateVerificationRequest(
    val subject: Subject
)

data class Subject(
    val identity: String,
    val type: String
)

data class CreateVerificationResponse(
    val id: String
)

data class ConfirmVerificationRequest(
    val verificationId: String?,
    val code: String
)