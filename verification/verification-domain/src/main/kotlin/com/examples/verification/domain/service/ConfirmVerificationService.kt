package com.examples.verification.domain.service

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.ConfirmVerificationResult
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ConfirmVerificationService {
    fun confirm(command: ConfirmVerificationCommand): Mono<ConfirmVerificationResult> {
        TODO("Not yet implemented")
    }


}
