package com.examples.verification.domain.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(ApplicationProperties::class)
open class ApplicationPropertiesConfiguration {

}