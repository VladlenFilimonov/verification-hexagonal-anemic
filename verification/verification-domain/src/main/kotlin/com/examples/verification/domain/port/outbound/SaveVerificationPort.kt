package com.examples.verification.domain.port.outbound

import com.examples.verification.domain.model.Verification
import reactor.core.publisher.Mono

interface SaveVerificationPort {
    fun save(verification: Verification): Mono<Verification>

}
