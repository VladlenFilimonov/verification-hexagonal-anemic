package com.examples.verification.domain.service

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.ReadVerificationPort
import com.examples.verification.domain.port.outbound.SaveVerificationAttemptsPort
import com.examples.verification.domain.port.outbound.UpdateVerificationPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ConfirmVerificationService(
    private val readVerificationPort: ReadVerificationPort,
    private val updateVerificationPort: UpdateVerificationPort,
    private val saveVerificationAttemptPort: SaveVerificationAttemptsPort
) {
    fun confirm(cmd: ConfirmVerificationCommand): Mono<Verification> {
        return readVerificationPort.read(cmd.id)
            .flatMap { compareCodes(it, cmd) }
            .map { setConfirmed(it) }
            .flatMap { updateVerificationPort.update(it) }
            .switchIfEmpty(throwNotFoundError())
    }

    private fun compareCodes(verification: Verification, cmd: ConfirmVerificationCommand): Mono<Verification> {
        return Mono.just(verification)
            .filter { it.code == cmd.code }
            .switchIfEmpty(Mono.defer { processCodeComparisonError(verification) }) //Mono.defer is needed to avoid redundant Port call
    }

    private fun processCodeComparisonError(verification: Verification): Mono<Verification> {
        return saveVerificationAttemptPort.save(verification)
            .flatMap { throwCodeComparisonError() }
    }

    private fun throwCodeComparisonError(): Mono<Verification> {
        return Mono.error(
            VerificationError(
                "Verification code doesn't match",
                ErrorCode.VERIFICATION_CODE_NOT_MATCH
            )
        )
    }

    private fun setConfirmed(verification: Verification): Verification {
        return verification.copy(confirmed = true)
    }

    private fun throwNotFoundError(): Mono<Verification> {
        return Mono.error(VerificationError("Verification not found", ErrorCode.VERIFICATION_NOT_FOUND))
    }

}
