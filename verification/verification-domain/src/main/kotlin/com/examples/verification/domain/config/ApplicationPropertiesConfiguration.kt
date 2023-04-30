package com.examples.verification.domain.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ApplicationPropertiesConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "verification")
    fun applicationProperties(): ApplicationProperties = ApplicationProperties()
}