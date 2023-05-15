package com.examples.verification.domain.validation

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.CreateVerificationCommand
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class VerificationValidationService(
    private val subjectValidator: SubjectValidator,
    private val userInfoValidator: UserInfoValidator
) {

    fun validate(command: CreateVerificationCommand): Mono<CreateVerificationCommand> {
        return Mono.fromSupplier { validateCreateVerificationCommand(command) }
    }

    fun validate(command: ConfirmVerificationCommand): Mono<ConfirmVerificationCommand> {
        return Mono.fromSupplier { validateConfirmVerificationCommand(command) }
    }

    private fun validateCreateVerificationCommand(command: CreateVerificationCommand): CreateVerificationCommand {
        subjectValidator.validate(command.subject)
        userInfoValidator.validate(command.userInfo)
        return command
    }

    private fun validateConfirmVerificationCommand(command: ConfirmVerificationCommand): ConfirmVerificationCommand {
        userInfoValidator.validate(command.userInfo)
        return command
    }

}
