package com.examples.verification.domain.service.generator.impl

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NumericCodeGeneratorTest {

    private val codeGenerator: NumericCodeGenerator = NumericCodeGenerator()

    @Test
    fun `generate should return a numeric code of the specified length`() {
        // Arrange
        val length = 6

        // Act
        val code = codeGenerator.generate(length)

        // Assert
        assertEquals(length, code.length)
        assertEquals(true, code.toIntOrNull() != null)
    }

    @Test
    fun `generate should generate unique codes for different lengths`() {
        // Arrange
        val length1 = 6
        val length2 = 8

        // Act
        val code1 = codeGenerator.generate(length1)
        val code2 = codeGenerator.generate(length2)

        // Assert
        assertEquals(length1, code1.length)
        assertEquals(true, code1.toIntOrNull() != null)

        assertEquals(length2, code2.length)
        assertEquals(true, code2.toIntOrNull() != null)

        assertEquals(false, code1 == code2)
    }
}