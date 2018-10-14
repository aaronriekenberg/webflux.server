package org.aaron.webflux.server.web

import mu.KLogging
import org.aaron.webflux.server.service.CommandService
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
        return commandService.runCommand(id).map { commandResult ->
            Rendering.view("command")
                    .modelAttribute("commandResult", commandResult)
                    .status(HttpStatus.OK)
                    .build()
        }.switchIfEmpty(Mono.just(
                Rendering.view("error/404")
                        .status(HttpStatus.NOT_FOUND)
                        .build()
        ))
    }
}