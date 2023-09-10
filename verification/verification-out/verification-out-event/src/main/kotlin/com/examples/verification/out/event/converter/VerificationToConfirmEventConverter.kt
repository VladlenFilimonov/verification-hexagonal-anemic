package com.examples.verification.out.event.converter

import com.examples.verification.domain.model.Verification
import com.examples.verification.out.event.model.ConfirmVerificationEvent
import com.examples.verification.out.event.model.OccurredOn
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class VerificationToConfirmEventConverter : Converter<Verification, ConfirmVerificationEvent> {
    override fun convert(source: Verification): ConfirmVerificationEvent {
        return ConfirmVerificationEvent(
            source.id.toString(),
            source.code,
            source.subject,
            OccurredOn.CONFIRM_VERIFICATION
        )
    }

}