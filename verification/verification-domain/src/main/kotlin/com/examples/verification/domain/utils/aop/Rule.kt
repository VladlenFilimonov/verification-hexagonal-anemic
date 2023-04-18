package com.examples.verification.domain.utils.aop

import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class Rule