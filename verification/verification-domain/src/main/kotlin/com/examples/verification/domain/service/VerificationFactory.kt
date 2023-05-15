package com.examples.verification.domain.service

import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.config.ApplicationProperties
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.service.generator.VerificationCodeGenerator
import java.util.UUID
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class VerificationFactory(
    private val applicationProperties: ApplicationProperties,
    private val codeGenerator: VerificationCodeGenerator
) {
    fun buildVerification(cmd: CreateVerificationCommand): Mono<Verification> {
        return Mono.fromSupplier {
            Verification(
                id = UUID.randomUUID(),
                confirmed = false,
                expired = false,
                code = codeGenerator.generate(applicationProperties.verificationCodeLength),
                subject = cmd.subject,
                userInfo = cmd.userInfo
            )
        }
    }

}
