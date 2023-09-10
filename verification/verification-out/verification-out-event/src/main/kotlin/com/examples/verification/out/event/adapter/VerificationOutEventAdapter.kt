package com.examples.verification.out.event.adapter

import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.ConfirmVerificationEventPort
import com.examples.verification.domain.port.outbound.CreateVerificationEventPort
import com.examples.verification.domain.utils.aop.Adapter
import com.examples.verification.out.event.model.ConfirmVerificationEvent
import com.examples.verification.out.event.model.CreateVerificationEvent
import com.examples.verification.out.event.model.VerificationOutTopic.CONFIRM_VERIFICATION_TOPIC
import com.examples.verification.out.event.model.VerificationOutTopic.CREATE_VERIFICATION_TOPIC
import org.springframework.core.convert.ConversionService
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.core.publisher.Mono

@Adapter
class VerificationOutEventAdapter(
    private val createVerificationEventTemplate: ReactiveKafkaProducerTemplate<String, CreateVerificationEvent>,
    private val confirmVerificationEventTemplate: ReactiveKafkaProducerTemplate<String, ConfirmVerificationEvent>,
    private val conversionService: ConversionService
) : CreateVerificationEventPort, ConfirmVerificationEventPort {
    override fun sendCreate(verification: Verification): Mono<Verification> {
        return Mono.fromSupplier { conversionService.convert(verification, CreateVerificationEvent::class.java) }
            .flatMap { createVerificationEventTemplate.send(CREATE_VERIFICATION_TOPIC.name, it) }
            .then(Mono.just(verification))
    }

    override fun sendConfirm(verification: Verification): Mono<Verification> {
        return Mono.fromSupplier { conversionService.convert(verification, ConfirmVerificationEvent::class.java) }
            .flatMap { confirmVerificationEventTemplate.send(CONFIRM_VERIFICATION_TOPIC.name, it) }
            .then(Mono.just(verification))
    }
}