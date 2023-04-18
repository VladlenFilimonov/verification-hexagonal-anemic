package com.examples.verification.domain.port.inbound

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.ConfirmVerificationResult
import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.api.CreateVerificationResult
import reactor.core.publisher.Mono

interface ConfirmVerificationUseCase {
    fun confirm(command: ConfirmVerificationCommand): Mono<ConfirmVerificationResult>
}

interface CreateVerificationUseCase {
    fun create(command: CreateVerificationCommand): Mono<CreateVerificationResult>
}