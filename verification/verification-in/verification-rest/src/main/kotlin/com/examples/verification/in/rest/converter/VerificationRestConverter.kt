package com.examples.verification.`in`.rest.converter

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.api.CreateVerificationResult
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.SubjectType
import com.examples.verification.`in`.rest.api.ConfirmVerificationRequest
import com.examples.verification.`in`.rest.api.CreateVerificationRequest
import com.examples.verification.`in`.rest.api.CreateVerificationResponse
import com.examples.verification.`in`.rest.api.UserInfo
import java.util.UUID
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class CreateVerificationRequestToCommand : Converter<CreateVerificationRequest, CreateVerificationCommand> {
    override fun convert(source: CreateVerificationRequest): CreateVerificationCommand {
        val subject = Subject(source.subject.identity, SubjectType.valueOf(source.subject.type))
        val userInfo = source.userInfo ?: UserInfo("", "")
        return CreateVerificationCommand(
            subject,
            com.examples.verification.domain.model.UserInfo(
                userInfo.ipAddress,
                userInfo.userAgent
            )
        )
    }
}

@Component
class CreateVerificationResultToResponse : Converter<CreateVerificationResult, CreateVerificationResponse> {
    override fun convert(source: CreateVerificationResult): CreateVerificationResponse {
        return CreateVerificationResponse(source.id.toString())
    }
}

@Component
class ConfirmVerificationRequestToCommand : Converter<ConfirmVerificationRequest, ConfirmVerificationCommand> {
    override fun convert(source: ConfirmVerificationRequest): ConfirmVerificationCommand {
        val verificationId = UUID.fromString(source.verificationId)
        val userInfo = source.userInfo ?: UserInfo("", "")
        return ConfirmVerificationCommand(
            verificationId,
            source.code,
            com.examples.verification.domain.model.UserInfo(
                userInfo.ipAddress,
                userInfo.userAgent
            )
        )
    }
}
