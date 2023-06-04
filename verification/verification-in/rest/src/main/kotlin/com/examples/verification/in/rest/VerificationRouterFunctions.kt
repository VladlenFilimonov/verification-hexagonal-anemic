package com.examples.verification.`in`.rest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.PUT
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class VerificationRouterFunctions(
    private val verificationRestAdapter: VerificationRestAdapter
) {
    @Bean
    fun verifyRouter(): RouterFunction<ServerResponse> {
        return route(POST("/verifications"), handleCreate())
            .andRoute(PUT("/verifications/{$VERIFICATION_ID_PATH_VARIABLE_NAME}/confirm"), handleConfirm())
    }

    private fun handleCreate(): (ServerRequest) -> Mono<ServerResponse> {
        return { request -> verificationRestAdapter.create(request) }
    }

    private fun handleConfirm(): (ServerRequest) -> Mono<ServerResponse> {
        return { request -> verificationRestAdapter.confirm(request) }
    }
}

const val VERIFICATION_ID_PATH_VARIABLE_NAME = "verification_id"