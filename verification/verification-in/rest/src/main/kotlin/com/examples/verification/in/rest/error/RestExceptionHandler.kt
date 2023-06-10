package com.examples.verification.`in`.rest.error

import com.examples.commons.utils.LoggingUtils.Companion.logErrorWithTraceId
import com.examples.verification.domain.error.ErrorCode.INVALID_SUBJECT
import com.examples.verification.domain.error.ErrorCode.INVALID_USER_INFO
import com.examples.verification.domain.error.ErrorCode.IP_ADDRESS_NOT_MATCH
import com.examples.verification.domain.error.ErrorCode.USER_AGENT_NOT_MATCH
import com.examples.verification.domain.error.ErrorCode.VERIFICATION_CODE_NOT_MATCH
import com.examples.verification.domain.error.ErrorCode.VERIFICATION_DUPLICATION
import com.examples.verification.domain.error.ErrorCode.VERIFICATION_EXPIRED
import com.examples.verification.domain.error.ErrorCode.VERIFICATION_EXPIRED_BY_MAX_ATTEMPTS
import com.examples.verification.domain.error.ErrorCode.VERIFICATION_IDEMPOTENT_RULE_VIOLATION
import com.examples.verification.domain.error.ErrorCode.VERIFICATION_NOT_FOUND
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.`in`.rest.api.RestError
import org.springframework.core.codec.CodecException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class RestExceptionHandler {

    fun handle(error: Throwable): Mono<ServerResponse> {
        return resolveException(error)
            .doOnEach { signal -> logErrorWithTraceId(error, signal, RestExceptionHandler::class.java) }
    }

    private fun resolveException(error: Throwable): Mono<ServerResponse> {
        return when (error) {
            is VerificationError -> handleVerificationError(error)
            is CodecException -> handleDeserializationError()
            else -> handleUnexpectedError(error)
        }
    }

    private fun handleVerificationError(error: VerificationError): Mono<ServerResponse> {
        return when (error.errorCode) {
            INVALID_SUBJECT -> ServerResponse
                .status(422)
                .bodyValue(RestError(error.errorCode.toString(), "Validation failed: invalid subject supplied"))

            INVALID_USER_INFO -> wrapBadRequest(error)
            VERIFICATION_EXPIRED -> wrapBadRequest(error)
            VERIFICATION_NOT_FOUND -> wrapBadRequest(error)
            VERIFICATION_DUPLICATION -> ServerResponse
                .status(409)
                .bodyValue(RestError(error.errorCode.toString(), "Duplicated verification"))

            USER_AGENT_NOT_MATCH -> wrapBadRequest(error)
            IP_ADDRESS_NOT_MATCH -> wrapBadRequest(error)
            VERIFICATION_EXPIRED_BY_MAX_ATTEMPTS -> wrapBadRequest(error)
            VERIFICATION_IDEMPOTENT_RULE_VIOLATION -> wrapBadRequest(error)
            VERIFICATION_CODE_NOT_MATCH -> wrapBadRequest(error)
            else -> wrapBadRequest(error)
        }
    }

    private fun handleUnexpectedError(error: Throwable): Mono<ServerResponse> {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .bodyValue(RestError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), error.message))
    }

    private fun handleDeserializationError(): Mono<ServerResponse> {
        return ServerResponse
            .badRequest()
            .bodyValue(RestError("SERIALIZATION_ERROR", "Malformed JSON passed"))
    }

    private fun wrapBadRequest(error: VerificationError): Mono<ServerResponse> {
        return ServerResponse
            .badRequest()
            .bodyValue(RestError(error.errorCode.toString(), error.message))
    }

}