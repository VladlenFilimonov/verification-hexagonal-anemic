package com.examples.verification.domain.service.generator

import java.util.UUID

class VerificationIdGenerator {

    fun generate(): UUID {
        return UUID.randomUUID()
    }
}
