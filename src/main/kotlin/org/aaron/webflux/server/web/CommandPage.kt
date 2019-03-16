package org.aaron.webflux.server.web

import mu.KLogging
import org.aaron.webflux.server.service.CommandService
import org.aaron.webflux.server.util.notFoundRenderingViewIfEmpty
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.reactive.result.view.Rendering
import reactor.core.publisher.Mono

@Controller
class CommandPage(
        private val commandService: CommandService) {

    companion object : KLogging()

    @GetMapping("/commands/{id}")
    fun command(@PathVariable(required = true) id: String): Mono<Rendering> {
        return commandService.getById(id).map { command ->
            Rendering.view("command").apply {
                modelAttribute("command", command)
                status(HttpStatus.OK)
            }.build()
        }.notFoundRenderingViewIfEmpty()
    }
}