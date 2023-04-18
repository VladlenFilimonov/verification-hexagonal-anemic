package com.examples.verification.domain.port.outbound

import com.examples.verification.domain.api.ConfirmVerificationCommand
import com.examples.verification.domain.api.CreateVerificationCommand

interface ErrorEventVerificationPort {
    fun send(error: Throwable, command: ConfirmVerificationCommand): Throwable
    fun send(error: Throwable, command: CreateVerificationCommand): Throwable
}
