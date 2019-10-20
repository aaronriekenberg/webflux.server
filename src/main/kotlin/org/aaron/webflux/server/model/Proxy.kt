package org.aaron.webflux.server.model

import java.time.OffsetDateTime

data class MutableProxy(
        var id: String? = null,
        var description: String? = null,
        var url: String? = null,
        var maxParallelCalls: Int = 10) {

    fun toProxy(): Proxy {
        return Proxy(
                id = this.id!!,
                description = this.description!!,
                url = this.url!!,
                maxParallelCalls = this.maxParallelCalls
        )
    }

}

data class Proxy(
        val id: String,
        val description: String,
        val url: String,
        val maxParallelCalls: Int
)

data class ProxyAPIResult(
        val proxy: Proxy,
        val now: OffsetDateTime,
        val tries: Int,
        val responseBody: String,
        val responseHeaders: Map<String, List<String>> = emptyMap(),
        val responseStatus: Int = 0
)