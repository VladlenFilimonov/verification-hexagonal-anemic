package com.examples.commons.utils

import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Signal

class LoggingUtils {

    companion object {
        fun logErrorWithTraceId(error: Throwable, signal: Signal<ServerResponse>, clazz: Class<*>) {
            val log = LoggerFactory.getLogger(clazz)
            if (signal.isOnError or signal.isOnNext) {
                MDC.put("traceId", signal.contextView.getOrDefault("traceId", "not present"))
                log.error(error.message, error)
                MDC.clear()
            }
        }
    }
}