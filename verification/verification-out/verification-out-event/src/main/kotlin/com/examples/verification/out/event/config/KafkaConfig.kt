package com.examples.verification.out.event.config

import com.examples.verification.out.event.model.CreateVerificationEvent
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.sender.SenderOptions


@Configuration
class KafkaConfig {

    @Bean
    fun createVerificationEventProducerTemplate(properties: KafkaProperties): ReactiveKafkaProducerTemplate<String, CreateVerificationEvent> {
        val props = properties.buildProducerProperties()
        return ReactiveKafkaProducerTemplate<String, CreateVerificationEvent>(SenderOptions.create(props))
    }
}