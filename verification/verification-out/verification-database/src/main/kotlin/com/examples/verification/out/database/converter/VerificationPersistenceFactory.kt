package com.examples.verification.out.database.converter

import com.examples.verification.domain.config.ApplicationProperties
import com.examples.verification.domain.model.Subject
import com.examples.verification.domain.model.SubjectType
import com.examples.verification.domain.model.UserInfo
import com.examples.verification.domain.model.Verification
import com.examples.verification.out.database.entity.VerificationEntity
import java.time.OffsetDateTime
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono


@Component
class VerificationPersistenceFactory(
    private val applicationProperties: ApplicationProperties
) {
    fun createVerificationEntity(verification: Verification): Mono<VerificationEntity> {
        return Mono.fromSupplier {
            VerificationEntity(
                pk = null,
                id = verification.id,
                confirmed = verification.confirmed,
                expirationDateTime = OffsetDateTime.now().plus(applicationProperties.verificationTtl),
                code = verification.code,
                subjectIdentity = verification.subject.identity,
                subjectType = verification.subject.type.name,
                userIp = verification.userInfo.ipAddress,
                userAgent = verification.userInfo.userAgent,
                modifiedAt = OffsetDateTime.now()
            )
        }
    }

    fun createVerification(entity: VerificationEntity): Mono<Verification> {
        return Mono.fromSupplier {
            Verification(
                id = entity.id,
                confirmed = entity.confirmed,
                expired = OffsetDateTime.now().isAfter(entity.expirationDateTime),
                code = entity.code,
                subject = Subject(entity.subjectIdentity, SubjectType.valueOf(entity.subjectType)),
                userInfo = UserInfo(entity.userIp, entity.userAgent)
            )
        }
    }

    fun updateEntity(entity: VerificationEntity, verification: Verification): Mono<VerificationEntity> {
        return Mono.fromSupplier {
            entity.copy(
                confirmed = verification.confirmed,
                modifiedAt = OffsetDateTime.now()
            )
        }
    }
}