package org.aaron.webflux.server.web

import mu.KLogging
import org.aaron.webflux.server.service.CommandService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.reactive.result.view.Rendering

@Controller
class CommandPage(
        private val commandService: CommandService) {

    companion object : KLogging()

    @GetMapping("/commands/{id}")
    fun command(@PathVariable(required = true) id: String): Rendering {
        return Rendering.view("command")
                .modelAttribute("commandResult", commandService.runCommand(id))
                .status(HttpStatus.OK)
                .build()
    }
}