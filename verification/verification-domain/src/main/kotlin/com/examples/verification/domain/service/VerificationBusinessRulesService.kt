package com.examples.verification.domain.service

import com.examples.verification.domain.api.CreateVerificationCommand
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class VerificationBusinessRulesService {
    fun applyRules(cmd: CreateVerificationCommand): Mono<CreateVerificationCommand> {

    }

}
