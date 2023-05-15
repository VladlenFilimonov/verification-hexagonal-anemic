package com.examples.verification.domain.service

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.ReadVerificationPort
import com.examples.verification.domain.port.outbound.SaveVerificationPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ConfirmVerificationService(
    private val readVerificationPort: ReadVerificationPort,
    private val saveVerificationPort: SaveVerificationPort
) {
    fun confirm(cmd: ConfirmVerificationCommand): Mono<Verification> {
        return readVerificationPort.read(cmd.id)
            .map { verification -> compareCodes(verification, cmd) }
            .map { verification -> setConfirmed(verification) }
            .flatMap { verification -> saveVerificationPort.save(verification) }
            .switchIfEmpty(Mono.error(VerificationError("Verification not found", ErrorCode.VERIFICATION_NOT_FOUND)))
    }

    private fun compareCodes(verification: Verification, cmd: ConfirmVerificationCommand): Verification {
        if (cmd.code != verification.code) {
            throw VerificationError("Verification code doesn't match", ErrorCode.VERIFICATION_CODE_NOT_MATCH)
        }
        return verification
    }

    private fun setConfirmed(verification: Verification): Verification {
        return verification.copy(confirmed = true)
    }

}
