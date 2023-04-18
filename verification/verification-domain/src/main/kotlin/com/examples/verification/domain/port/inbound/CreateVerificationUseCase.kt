package com.examples.verification.domain.port.inbound

import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.api.CreateVerificationResult
import reactor.core.publisher.Mono

interface CreateVerificationUseCase {

    fun create(command: CreateVerificationCommand): Mono<CreateVerificationResult>

}