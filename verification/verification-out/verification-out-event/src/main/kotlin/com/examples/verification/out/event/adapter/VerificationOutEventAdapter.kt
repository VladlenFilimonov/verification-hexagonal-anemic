package com.examples.verification.out.event.adapter

import com.examples.verification.domain.model.Verification
import com.examples.verification.domain.port.outbound.CreateVerificationEventPort
import com.examples.verification.domain.utils.aop.Adapter
import com.examples.verification.out.event.model.CreateVerificationEvent
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.core.publisher.Mono

@Adapter
class VerificationOutEventAdapter(
    private val createVerificationEventTemplate: ReactiveKafkaProducerTemplate<String, CreateVerificationEvent>
) : CreateVerificationEventPort {
    override fun send(verification: Verification): Mono<Verification> {
        TODO("Not yet implemented")
    }
}