package org.aaron.webflux.server.filter

import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class LoggingFilter : WebFilter {

    companion object : KLogging()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        logger.debug { getRequestMessage(exchange) }

        exchange.response.beforeCommit {
            logger.info { getResponseMessage(exchange) }
            Mono.empty()
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

    private fun getResponseMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val remoteAddress = request.remoteAddress
        val response = exchange.response
        val method = request.method
        val path = request.uri.path
        val statusCode = response.statusCode
        return "$remoteAddress $method $path status=${statusCode?.value()}"
    }

}