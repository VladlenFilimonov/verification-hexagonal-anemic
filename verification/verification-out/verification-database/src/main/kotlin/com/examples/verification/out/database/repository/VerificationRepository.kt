package com.examples.verification.out.database.repository

import com.examples.verification.out.database.config.VERIFICATION_TABLE_NAME
import com.examples.verification.out.database.entity.VerificationEntity
import java.util.UUID
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface VerificationRepository : ReactiveCrudRepository<VerificationEntity, Long> {

    @Query(
        """
            SELECT v.* FROM $VERIFICATION_TABLE_NAME AS v  
            WHERE v.id = :id
        """
    )
    fun findByVerificationId(id: UUID?): Mono<VerificationEntity>

    @Query(
        """
            SELECT v.* FROM $VERIFICATION_TABLE_NAME AS v 
            WHERE v.subject_identity = :identity AND v.subject_type = :type
        """
    )
    fun findBySubject(identity: String, type: String): Mono<VerificationEntity>
}