package org.aaron.webflux.server.api

import mu.KLogging
import org.aaron.webflux.server.model.ProxyAPIResult
import org.aaron.webflux.server.service.ProxyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/proxies")
class ProxyController(
        private val proxyService: ProxyService) {

    companion object : KLogging()

    @GetMapping("/{id}")
    fun makeRequest(@PathVariable(required = true) id: String): Mono<ResponseEntity<ProxyAPIResult>> {
        logger.debug { "makeRequest id = ${id}" }
        return proxyService.makeRequest(id).map { proxyAPIResult ->
            ResponseEntity.ok(proxyAPIResult)
        }.switchIfEmpty(Mono.just(ResponseEntity(HttpStatus.NOT_FOUND)))
    }
}