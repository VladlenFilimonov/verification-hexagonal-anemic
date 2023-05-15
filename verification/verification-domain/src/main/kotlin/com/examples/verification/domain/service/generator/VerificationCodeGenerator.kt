package com.examples.verification.domain.service.generator

interface VerificationCodeGenerator {
    fun generate(length: Int): String
}
