package org.aaron.webflux.server.web

import mu.KLogging
import org.aaron.webflux.server.service.ProxyService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.reactive.result.view.Rendering
import reactor.core.publisher.Mono

@Controller
class ProxyPage(
        private val proxyService: ProxyService) {

    companion object : KLogging()

    @GetMapping("/proxies/{id}")
    fun command(@PathVariable(required = true) id: String): Mono<Rendering> {
        return proxyService.makeRequest(id).map { proxyResult ->
            Rendering.view("proxy").apply {
                modelAttribute("proxyResult", proxyResult)
                status(HttpStatus.OK)
            }.build()
        }.defaultIfEmpty(
                Rendering.view("error/404").apply {
                    status(HttpStatus.NOT_FOUND)
                }.build()
        )
    }
}