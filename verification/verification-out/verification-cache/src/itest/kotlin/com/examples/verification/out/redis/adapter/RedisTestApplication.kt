package com.examples.verification.out.redis.adapter

import com.examples.verification.domain.config.ApplicationPropertiesConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(
    scanBasePackages = ["com.examples.verification.out.redis"],
    scanBasePackageClasses = [ApplicationPropertiesConfiguration::class]
)
open class RedisTestApplication

fun main(args: Array<String>) {
    runApplication<RedisTestApplication>(*args)
}