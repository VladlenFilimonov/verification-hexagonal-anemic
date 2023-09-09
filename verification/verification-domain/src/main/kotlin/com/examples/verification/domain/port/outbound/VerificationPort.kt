package com.examples.verification.domain.port.outbound

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.model.VerificationAttempts
import java.util.UUID
import reactor.core.publisher.Mono

interface CreateVerificationPort {
    fun create(verification: Verification): Mono<Verification>
}

interface ReadVerificationPort {
    fun read(id: UUID): Mono<Verification>
    fun read(subject: Subject): Mono<Verification>
}

interface UpdateVerificationPort {
    fun update(verification: Verification): Mono<Verification>
}

interface CreateVerificationEventPort {
    fun send(verification: Verification): Mono<Verification>
}

interface ConfirmVerificationEventPort {
    fun send(verification: Verification): Mono<Verification>
}

interface ErrorVerificationEventPort {
    fun send(error: Throwable, command: ConfirmVerificationCommand): Throwable
    fun send(error: Throwable, command: CreateVerificationCommand): Throwable
}

interface ReadVerificationAttemptsPort {
    fun read(verificationId: UUID): Mono<VerificationAttempts>
}

interface SaveVerificationAttemptsPort {
    fun save(verification: Verification): Mono<Verification>
}

interface AcquireDistributedLockPort {
    fun acquire(cmd: ConfirmVerificationCommand): Mono<ConfirmVerificationCommand>
}
