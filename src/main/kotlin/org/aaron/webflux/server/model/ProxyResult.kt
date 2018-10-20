package org.aaron.webflux.server.model

import java.time.OffsetDateTime

data class ProxyResult(
        val proxy: Proxy,
        val startTime: OffsetDateTime,
        val responseBody: String,
        val responseHeaders: String,
        val responseStatus: Int
)