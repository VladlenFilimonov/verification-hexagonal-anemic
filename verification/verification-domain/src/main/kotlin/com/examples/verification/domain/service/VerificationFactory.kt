package com.examples.verification.domain.service

import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.config.ApplicationProperties
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.service.generator.VerificationCodeGenerator
import com.examples.verification.domain.service.generator.VerificationIdGenerator
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class VerificationFactory(
    private val applicationProperties: ApplicationProperties,
    private val codeGenerator: VerificationCodeGenerator,
    private val idGenerator: VerificationIdGenerator
) {
    fun buildVerification(cmd: CreateVerificationCommand): Mono<Verification> {
        return Mono.fromSupplier {
            Verification(
                id = idGenerator.generate(),
                confirmed = false,
                expired = false,
                code = codeGenerator.generate(applicationProperties.verificationCodeLength),
                subject = cmd.subject,
                userInfo = cmd.userInfo
            )
        }
    }

}
