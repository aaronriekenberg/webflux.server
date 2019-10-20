package org.aaron.webflux.server.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import mu.KotlinLogging
import org.aaron.webflux.server.config.ProxyConfig
import org.aaron.webflux.server.model.Proxy
import org.aaron.webflux.server.model.ProxyAPIResult
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import java.net.URI
import java.time.OffsetDateTime

private val logger = KotlinLogging.logger {}

private data class ProxyInfo(
        val proxy: Proxy,
        val semaphore: Semaphore
)

@Service
class ProxyService(
        proxyConfig: ProxyConfig,
        private val webClient: WebClient) {

    private val idToProxyInfo: Map<String, ProxyInfo> = proxyConfig.proxies
            .map {
                it.id to ProxyInfo(
                        proxy = it,
                        semaphore = Semaphore(permits = it.maxParallelCalls)
                )
            }
            .toMap()

    private val proxyList = idToProxyInfo.values.map { it.proxy }.toList()

    fun getById(id: String): Proxy? {
        return idToProxyInfo[id]?.proxy
    }

    fun getProxies(): List<Proxy> {
        return proxyList
    }

    suspend fun makeRequest(id: String): ProxyAPIResult? {
        val proxyInfo = idToProxyInfo[id] ?: return null

        val proxy = proxyInfo.proxy
        val uri = URI(proxy.url)

        var success = false
        var tries = 0
        var delayMS = proxy.retryDelayMS
        var lastResult: ProxyAPIResult? = null

        while ((!success) && (tries < proxy.maxTries)) {
            try {
                ++tries

                val clientResponse =
                        proxyInfo.semaphore.withPermit {
                            webClient.get()
                                    .uri(uri)
                                    .awaitExchange()
                        }

                val responseHeaders = clientResponse.headers().asHttpHeaders().toSortedMap()

                if (!clientResponse.statusCode().is2xxSuccessful) {
                    lastResult = ProxyAPIResult(
                            proxy = proxy,
                            now = OffsetDateTime.now(),
                            tries = tries,
                            responseBody = "Proxy Error",
                            responseHeaders = responseHeaders,
                            responseStatus = clientResponse.statusCode().value())
                } else {
                    val responseBody = clientResponse.awaitBody<String>()
                    lastResult = ProxyAPIResult(
                            proxy = proxy,
                            now = OffsetDateTime.now(),
                            tries = tries,
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
                        tries = tries,
                        responseBody = "Proxy Exception: ${e.javaClass}: ${e.message}")
            }

            if ((!success) && (tries < proxy.maxTries)) {
                logger.debug { "delay $delayMS" }
                delay(delayMS)
                delayMS *= 2L
            }
        }

        return lastResult
    }


}