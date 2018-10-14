package org.aaron.webflux.server.web

import mu.KLogging
import org.aaron.webflux.server.service.CommandService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.reactive.result.view.Rendering

@Controller
class IndexPage(
        private val commandService: CommandService) {

    companion object : KLogging()

    @GetMapping("/")
    fun index(): Rendering {
        return Rendering.view("index")
                .modelAttribute("commands", commandService.getCommands())
                .status(HttpStatus.OK)
                .build()
    }
}