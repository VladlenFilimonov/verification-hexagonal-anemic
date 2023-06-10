package com.examples.commons.utils

import java.util.UUID
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.util.context.Context

class TraceIdWebFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange)
            .contextWrite { ctx -> populateTraceId(ctx, exchange.request) }
    }

    private fun populateTraceId(ctx: Context, request: ServerHttpRequest): Context {
        val traceId = extractTraceIdFromHeader(request) ?: UUID.randomUUID()
        return ctx.put(TRACE_ID, traceId)
    }

    private fun extractTraceIdFromHeader(request: ServerHttpRequest): String? {
        return request.headers.getFirst(TRACE_ID)
    }
}