package org.aaron.webflux.server.web

import mu.KLogging
import org.aaron.webflux.server.service.ProxyService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.reactive.result.view.Rendering

@Controller
class ProxyPage(
        private val proxyService: ProxyService) {

    companion object : KLogging()

    @GetMapping("/proxies/{id}")
    fun command(@PathVariable(required = true) id: String): Rendering {
        val proxy = proxyService.getById(id)

        return if (proxy == null) {
            Rendering.view("error/404").apply {
                status(HttpStatus.NOT_FOUND)
            }.build()
        } else {
            Rendering.view("proxy").apply {
                modelAttribute("proxy", proxy)
                status(HttpStatus.OK)
            }.build()
        }
    }
}