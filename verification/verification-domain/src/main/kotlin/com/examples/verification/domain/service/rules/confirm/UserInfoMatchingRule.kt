package com.examples.verification.domain.service.rules.confirm

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.error.ErrorCode
import com.examples.verification.domain.error.VerificationError
import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.ReadVerificationPort
import com.examples.verification.domain.utils.aop.Rule
import reactor.core.publisher.Mono

@Rule
class UserInfoMatchingRule(
    private val readVerificationPort: ReadVerificationPort
) {
    fun apply(cmd: ConfirmVerificationCommand): Mono<ConfirmVerificationCommand> {
        return readVerificationPort.read(cmd.id)
            .map { verification -> matchUserInfo(verification, cmd) }
            .switchIfEmpty(Mono.error(VerificationError("Verification not found", ErrorCode.VERIFICATION_NOT_FOUND)))
    }

    private fun matchUserInfo(verification: Verification, cmd: ConfirmVerificationCommand): ConfirmVerificationCommand {
        if (verification.userInfo.userAgent != cmd.userInfo.userAgent) {
            throw VerificationError("User agent doesn't match", ErrorCode.USER_AGENT_NOT_MATCH)
        }
        if (verification.userInfo.ipAddress != cmd.userInfo.ipAddress) {
            throw VerificationError("IP address doesn't match", ErrorCode.IP_ADDRESS_NOT_MATCH)
        }
        return cmd
    }

}