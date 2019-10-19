package org.aaron.webflux.server.filter

import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.Instant

private val logger = KotlinLogging.logger {}

@Component
class LoggingFilter : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val startTime = Instant.now()
        logger.debug { getRequestMessage(exchange) }

        exchange.response.beforeCommit {
            Mono.fromRunnable {
                logger.info { getResponseMessage(exchange, startTime) }
            }
        }

        return chain.filter(exchange)
    }

    private fun getRequestMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val remoteAddress = request.remoteAddress
        val method = request.method
        val path = request.uri.path
        val acceptableMediaTypes = request.headers.accept
        val contentType = request.headers.contentType
        return ">>> $remoteAddress $method $path ${HttpHeaders.ACCEPT}: $acceptableMediaTypes ${HttpHeaders.CONTENT_TYPE}: $contentType"
    }

    private fun getResponseMessage(exchange: ServerWebExchange, startTime: Instant): String {
        val endTime = Instant.now()
        val request = exchange.request
        val remoteAddress = request.remoteAddress
        val response = exchange.response
        val method = request.method
        val path = request.uri.path
        val statusCode = response.statusCode
        val delta = (Duration.between(startTime, endTime).toNanos() / 1e9)
        return "$remoteAddress $method $path status=${statusCode?.value()} delta=${delta}s"
    }

}