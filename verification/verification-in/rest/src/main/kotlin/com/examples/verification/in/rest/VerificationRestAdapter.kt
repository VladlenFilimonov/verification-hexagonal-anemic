package com.examples.verification.`in`.rest

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.config.ApplicationProperties
import com.examples.verification.domain.port.inbound.ConfirmVerificationUseCase
import com.examples.verification.domain.port.inbound.CreateVerificationUseCase
import com.examples.verification.domain.utils.aop.Adapter
import com.examples.verification.`in`.rest.api.ConfirmVerificationRequest
import com.examples.verification.`in`.rest.api.CreateVerificationRequest
import com.examples.verification.`in`.rest.api.CreateVerificationResponse
import org.springframework.core.convert.ConversionService
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Adapter
class VerificationRestAdapter(
    private val createVerificationUseCase: CreateVerificationUseCase,
    private val confirmVerificationUseCase: ConfirmVerificationUseCase,
    private val exceptionHandler: RestExceptionHandler,
    private val applicationProperties: ApplicationProperties,
    private val conversionService: ConversionService
) {
    fun create(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(CreateVerificationRequest::class.java)
            .map { conversionService.convert(it, CreateVerificationCommand::class.java) }
            .flatMap(createVerificationUseCase::create)
            .map { conversionService.convert(it, CreateVerificationResponse::class.java) }
            .flatMap(::wrapToServerResponse)
            .timeout(applicationProperties.requestTimeout)
            .onErrorResume(exceptionHandler::handle)
    }

    fun confirm(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(ConfirmVerificationRequest::class.java)
            .map { dto -> withVerificationId(dto, request) }
            .map { conversionService.convert(it, ConfirmVerificationCommand::class.java) }
            .flatMap(confirmVerificationUseCase::confirm)
            .flatMap { ServerResponse.noContent().build() }
            .timeout(applicationProperties.requestTimeout)
            .onErrorResume(exceptionHandler::handle)
    }

    private fun withVerificationId(
        dto: ConfirmVerificationRequest,
        request: ServerRequest
    ): ConfirmVerificationRequest {
        val pathVariable = request.pathVariable(VERIFICATION_ID_PATH_VARIABLE_NAME)
        return dto.copy(verificationId = pathVariable)
    }

    private fun wrapToServerResponse(response: Any): Mono<ServerResponse> {
        return ServerResponse
            .ok()
            .bodyValue(response)
    }
}