package com.examples.verification.`in`.rest

import com.examples.verification.domain.config.ApplicationProperties
import com.examples.verification.`in`.rest.adapter.VerificationRestAdapter
import com.examples.verification.`in`.rest.error.RestExceptionHandler
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
    private val verificationRestAdapter: VerificationRestAdapter,
    private val applicationProperties: ApplicationProperties,
    private val exceptionHandler: RestExceptionHandler
) {
    @Bean
    fun verifyRouter(): RouterFunction<ServerResponse> {
        return route(POST("/verifications"), handleCreate())
            .andRoute(PUT("/verifications/{$VERIFICATION_ID_PATH_VARIABLE_NAME}/confirm"), handleConfirm())
    }

    private fun handleCreate(): (ServerRequest) -> Mono<ServerResponse> {
        return { request ->
            verificationRestAdapter.create(request)
                .timeout(applicationProperties.requestTimeout)
                .onErrorResume(exceptionHandler::handle)
        }
    }

    private fun handleConfirm(): (ServerRequest) -> Mono<ServerResponse> {
        return { request ->
            verificationRestAdapter.confirm(request)
                .timeout(applicationProperties.requestTimeout)
                .onErrorResume(exceptionHandler::handle)
        }
    }
}

const val VERIFICATION_ID_PATH_VARIABLE_NAME = "verification_id"