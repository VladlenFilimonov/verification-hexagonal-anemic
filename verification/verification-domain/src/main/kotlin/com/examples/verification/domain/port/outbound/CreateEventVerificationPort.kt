package com.examples.verification.domain.port.outbound

import com.examples.verification.domain.model.Verification
import reactor.core.publisher.Mono

interface CreateEventVerificationPort {

    fun send(verification: Verification): Mono<Verification>

}
