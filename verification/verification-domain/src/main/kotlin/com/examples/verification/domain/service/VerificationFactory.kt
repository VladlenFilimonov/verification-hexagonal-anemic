package com.examples.verification.domain.service

import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.model.Verification
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class VerificationFactory {
    fun buildVerification(cmd: CreateVerificationCommand): Mono<Verification> {
        TODO("not implemented")
    }

}
