package com.examples.verification.`in`.rest

import com.examples.verification.`in`.rest.api.UserInfo
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class UserInfoExtractor {

    fun extract(request: ServerRequest): UserInfo {
        val ipAddress = extractIp(request)
        val userAgent = extractAgent(request)
        return UserInfo(ipAddress, userAgent)
    }

    private fun extractIp(request: ServerRequest): String {
        return request.remoteAddress()
            .map { it.address.hostAddress }
            .orElse("Unknown IP")
    }

    private fun extractAgent(request: ServerRequest): String {
        return request.headers()
            .firstHeader("User-Agent") ?: "Unknown User-Agent"
    }
}
