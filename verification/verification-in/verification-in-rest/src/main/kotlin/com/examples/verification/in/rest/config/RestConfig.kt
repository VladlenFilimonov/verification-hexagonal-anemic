package com.examples.verification.`in`.rest.config

import com.examples.commons.utils.TraceIdWebFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.server.WebFilter

@Configuration
class RestConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun traceIdWebFilter(): WebFilter {
        return TraceIdWebFilter()
    }
}