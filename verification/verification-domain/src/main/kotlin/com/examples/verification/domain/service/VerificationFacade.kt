package com.examples.verification.domain.service

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.ConfirmVerificationResult
import com.examples.verification.domain.api.CreateVerificationCommand
import com.examples.verification.domain.api.CreateVerificationResult
import com.examples.verification.domain.port.inbound.ConfirmVerificationUseCase
import com.examples.verification.domain.port.inbound.CreateVerificationUseCase
import com.examples.verification.domain.port.outbound.ErrorEventVerificationPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class VerificationFacade(
    private val createVerificationFacade: CreateVerificationFacade,
    private val confirmVerificationFacade: ConfirmVerificationFacade,
    private val errorEventVerificationPort: ErrorEventVerificationPort
) : CreateVerificationUseCase, ConfirmVerificationUseCase {

    override fun confirm(command: ConfirmVerificationCommand): Mono<ConfirmVerificationResult> {
        return confirmVerificationFacade.confirm(command)
            .onErrorMap { error -> errorEventVerificationPort.send(error, command) }
    }

    override fun create(command: CreateVerificationCommand): Mono<CreateVerificationResult> {
        return createVerificationFacade.create(command)
            .onErrorMap { error -> errorEventVerificationPort.send(error, command) }
    }

}