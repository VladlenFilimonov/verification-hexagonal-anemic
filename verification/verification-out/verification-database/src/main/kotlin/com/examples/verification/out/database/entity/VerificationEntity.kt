package com.examples.verification.out.database.entity

import com.examples.verification.out.database.config.VERIFICATION_TABLE_NAME
import java.time.OffsetDateTime
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


@Table(name = VERIFICATION_TABLE_NAME)
data class VerificationEntity(

    @Id
    @Column("pk")
    val pk: Long?,

    @Column("id")
    val id: UUID?,

    @Column("confirmed")
    val confirmed: Boolean,

    @Column("expiration_date")
    val expirationDateTime: OffsetDateTime?,

    @Column("code")
    val code: String?,

    @Column("subject_type")
    val subjectType: String,

    @Column("subject_identity")
    val subjectIdentity: String,

    @Column("user_ip")
    val userIp: String,

    @Column("user_agent")
    val userAgent: String,

    @Column("modified_at")
    val modifiedAt: OffsetDateTime?

)