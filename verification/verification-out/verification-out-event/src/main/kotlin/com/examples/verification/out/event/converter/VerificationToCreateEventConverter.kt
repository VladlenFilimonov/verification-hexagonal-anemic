package com.examples.verification.out.event.converter

import com.examples.verification.domain.model.Verification
import com.examples.verification.out.event.model.CreateVerificationEvent
import com.examples.verification.out.event.model.OccurredOn
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class VerificationToCreateEventConverter : Converter<Verification, CreateVerificationEvent> {
    override fun convert(source: Verification): CreateVerificationEvent {
        return CreateVerificationEvent(
            source.id.toString(),
            source.code,
            source.subject,
            OccurredOn.CREATE_VERIFICATION
        )
    }
}