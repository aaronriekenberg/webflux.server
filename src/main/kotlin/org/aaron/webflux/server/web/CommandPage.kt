package org.aaron.webflux.server.web

import mu.KotlinLogging
import org.aaron.webflux.server.service.CommandService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.reactive.result.view.Rendering

private val logger = KotlinLogging.logger {}

@Controller
class CommandPage(
        private val commandService: CommandService) {

    @GetMapping("/commands/{id}")
    fun command(@PathVariable(required = true) id: String): Rendering {
        val command = commandService.getById(id)

        return if (command == null) {
            Rendering.view("error/404").apply {
                status(HttpStatus.NOT_FOUND)
            }.build()
        } else {
            Rendering.view("command").apply {
                modelAttribute("command", command)
                status(HttpStatus.OK)
            }.build()
        }
    }
}