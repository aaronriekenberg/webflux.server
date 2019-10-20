package org.aaron.webflux.server.model

import java.time.OffsetDateTime

data class ProxyConfiguration(
        val id: String,
        val description: String,
        val url: String,
        val maxParallelCalls: Int = 10,
        val maxTries: Int = 1,
        val retryDelayMS: Long = 100L
)

data class ProxyAPIResult(
        val proxy: ProxyConfiguration,
        val now: OffsetDateTime,
        val tries: Int,
        val responseBody: String,
        val responseHeaders: Map<String, List<String>> = emptyMap(),
        val responseStatus: Int = 0
)