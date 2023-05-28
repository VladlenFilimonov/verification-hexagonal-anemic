package com.examples.verification.domain.service.generator

import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class VerificationIdGeneratorTest {

    @Test
    fun `generate should return a unique UUID each time`() {

        val generator = VerificationIdGenerator()

        val id1 = generator.generate()
        val id2 = generator.generate()

        assertNotEquals(id1, id2)

    }
}