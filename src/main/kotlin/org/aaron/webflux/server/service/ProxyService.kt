package org.aaron.webflux.server.service

import kotlinx.coroutines.delay
import mu.KotlinLogging
import org.aaron.webflux.server.config.ProxyConfig
import org.aaron.webflux.server.model.MutableProxy
import org.aaron.webflux.server.model.Proxy
import org.aaron.webflux.server.model.ProxyAPIResult
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import java.net.URI
import java.time.OffsetDateTime

private val logger = KotlinLogging.logger {}

@Service
class ProxyService(
        private val proxyConfig: ProxyConfig,
        private val webClient: WebClient) {

    private val idToProxy: Map<String, Proxy> = proxyConfig.proxies
            .asSequence()
            .map(MutableProxy::toProxy)
            .map { it.id to it }
            .toMap()

    fun getById(id: String): Proxy? {
        return idToProxy[id]
    }

    fun getProxies(): Collection<Proxy> {
        return idToProxy.values
    }

    suspend fun makeRequest(id: String): ProxyAPIResult? {
        val proxy = getById(id) ?: return null

        val uri = URI(proxy.url)

        var success = false
        var tries = 0
        var delayMS = proxyConfig.retryDelayMS
        var lastResult: ProxyAPIResult? = null

        while ((!success) && (tries < proxyConfig.maxRetries)) {
            try {
                ++tries

                val clientResponse = webClient.get()
                        .uri(uri)
                        .awaitExchange()

                val responseHeaders = clientResponse.headers().asHttpHeaders().toSortedMap()

                if (!clientResponse.statusCode().is2xxSuccessful) {
                    lastResult = ProxyAPIResult(
                            proxy = proxy,
                            now = OffsetDateTime.now(),
                            responseBody = "Proxy Error",
                            responseHeaders = responseHeaders,
                            responseStatus = clientResponse.statusCode().value())
                } else {
                    val responseBody = clientResponse.awaitBody<String>()
                    lastResult = ProxyAPIResult(
                            proxy = proxy,
                            now = OffsetDateTime.now(),
                            responseBody = responseBody,
                            responseHeaders = responseHeaders,
                            responseStatus = clientResponse.statusCode().value())
                    success = true
                }
            } catch (e: Exception) {
                logger.warn(e) { "makeRequest" }
                lastResult = ProxyAPIResult(
                        proxy = proxy,
                        now = OffsetDateTime.now(),
                        responseBody = "Proxy Exception: ${e.javaClass}: ${e.message}")
            }

            if ((!success) && (tries < proxyConfig.maxRetries)) {
                logger.info { "delay $delayMS" }
                delay(delayMS)
                delayMS *= 2L
            }
        }

        return lastResult
    }


}