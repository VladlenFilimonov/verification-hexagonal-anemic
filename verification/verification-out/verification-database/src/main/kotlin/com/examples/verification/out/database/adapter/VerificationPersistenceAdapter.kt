package com.examples.verification.out.database.adapter

import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.CreateVerificationPort
import com.examples.verification.domain.port.outbound.ReadVerificationPort
import com.examples.verification.domain.port.outbound.UpdateVerificationPort
import com.examples.verification.domain.utils.aop.Adapter
import com.examples.verification.out.database.converter.VerificationPersistenceFactory
import com.examples.verification.out.database.repository.VerificationRepository
import java.util.UUID
import reactor.core.publisher.Mono

@Adapter
class VerificationPersistenceAdapter(
    private val verificationRepository: VerificationRepository,
    private val persistenceFactory: VerificationPersistenceFactory
) : CreateVerificationPort, ReadVerificationPort, UpdateVerificationPort {
    override fun create(verification: Verification): Mono<Verification> {
        return persistenceFactory.createVerificationEntity(verification)
            .flatMap(verificationRepository::save)
            .map { verification }
    }

    override fun read(id: UUID): Mono<Verification> {
        return verificationRepository.findByVerificationId(id)
            .flatMap(persistenceFactory::createVerification)
            .switchIfEmpty(throwVerificationNotFoundError())
    }

    override fun read(subject: Subject): Mono<Verification> {
        return verificationRepository.findBySubject(subject.identity, subject.type.name)
            .flatMap(persistenceFactory::createVerification)
            .switchIfEmpty(throwVerificationNotFoundError())
    }

    override fun update(verification: Verification): Mono<Verification> {
        return verificationRepository.findByVerificationId(verification.id)
            .flatMap { persistenceFactory.updateEntity(it, verification) }
            .map { verification }
            .switchIfEmpty(throwVerificationNotFoundError())
    }

    private fun throwVerificationNotFoundError(): Mono<Verification> {
        return Mono.error(VerificationError("Verification not found", ErrorCode.VERIFICATION_NOT_FOUND))
    }
}