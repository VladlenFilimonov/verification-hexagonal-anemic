package com.examples.verification.`in`.rest

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class RestExceptionHandler {
    fun handle(error: Throwable): Mono<ServerResponse> {
        return ServerResponse
            .badRequest()
            .bodyValue(error.message.orEmpty())
    }
}