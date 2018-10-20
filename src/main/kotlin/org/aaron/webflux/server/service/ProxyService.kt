package org.aaron.webflux.server.service

import mu.KLogging
import org.aaron.webflux.server.config.ProxyConfig
import org.aaron.webflux.server.model.MutableProxy
import org.aaron.webflux.server.model.Proxy
import org.aaron.webflux.server.model.ProxyResult
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.net.URI
import java.time.OffsetDateTime

@Service
class ProxyService(
        proxyConfig: ProxyConfig,
        private val webClient: WebClient) {

    companion object : KLogging()

    private val idToProxy: Map<String, Proxy> = proxyConfig.proxies
            .asSequence()
            .map(MutableProxy::toProxy)
            .map { it.id to it }
            .toMap()

    fun getById(id: String): Mono<Proxy> {
        return Mono.justOrEmpty(idToProxy[id])
    }

    fun getProxies(): Flux<Proxy> {
        return Flux.fromIterable(idToProxy.values)
    }

    fun makeRequest(id: String): Mono<ProxyResult> {
        return getById(id).flatMap { proxy ->
            val startTime = OffsetDateTime.now()
            val uri = URI(proxy.url)

            webClient.get()
                    .uri(uri)
                    .exchange()
                    .flatMap { clientResponse ->
                        val responseHeaders = clientResponse.headers().asHttpHeaders().toString()
                        if (!clientResponse.statusCode().is2xxSuccessful) {
                            ProxyResult(
                                    proxy = proxy,
                                    startTime = startTime,
                                    responseBody = "Proxy Error",
                                    responseHeaders = responseHeaders,
                                    responseStatus = clientResponse.statusCode().value()).toMono()
                        } else {
                            clientResponse.bodyToMono<String>()
                                    .map { responseBody ->
                                        ProxyResult(
                                                proxy = proxy,
                                                startTime = startTime,
                                                responseBody = responseBody,
                                                responseHeaders = responseHeaders,
                                                responseStatus = clientResponse.statusCode().value())
                                    }
                        }
                    }
        }
    }
}