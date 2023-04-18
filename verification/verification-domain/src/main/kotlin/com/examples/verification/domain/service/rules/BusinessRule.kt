package com.examples.verification.domain.service.rules

import reactor.core.publisher.Mono

interface BusinessRule<T> {

    fun apply(cmd: T): Mono<T>
    fun getOrder(): Int
}