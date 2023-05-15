package com.examples.verification.domain.service.generator.impl

import com.examples.verification.domain.service.generator.VerificationCodeGenerator
import kotlin.math.pow
import kotlin.random.Random
import org.springframework.stereotype.Component

@Component
class NumericCodeGenerator : VerificationCodeGenerator {
    override fun generate(length: Int): String {
        val min = 10.0.pow(length - 1).toInt()
        val max = 10.0.pow(length).toInt()
        val randomNumber = Random.nextInt(min, max)
        return String.format("%0${length}d", randomNumber)
    }
}