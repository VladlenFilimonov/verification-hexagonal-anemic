package com.examples.verification.domain.port.outbound

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.Verification
import java.util.UUID
import reactor.core.publisher.Mono

interface SaveVerificationPort {
    fun save(verification: Verification): Mono<Verification>
}

interface ReadVerificationPort {
    fun read(id: UUID): Mono<Verification>
    fun read(id: Subject): Mono<Verification>
}

interface CreateEventVerificationPort {
    fun send(verification: Verification): Mono<Verification>
}

interface ErrorEventVerificationPort {
    fun send(error: Throwable, command: ConfirmVerificationCommand): Throwable
    fun send(error: Throwable, command: CreateVerificationCommand): Throwable
}
