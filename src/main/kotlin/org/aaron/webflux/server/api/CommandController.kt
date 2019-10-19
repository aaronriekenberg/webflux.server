package org.aaron.webflux.server.api

import mu.KotlinLogging
import org.aaron.webflux.server.model.CommandAPIResult
import org.aaron.webflux.server.service.CommandService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/commands")
class CommandController(
        private val commandService: CommandService) {

    @GetMapping("/{id}")
    suspend fun runCommand(@PathVariable(required = true) id: String): ResponseEntity<CommandAPIResult> {
        logger.debug { "runCommand id = $id" }
        val commandResult = commandService.runCommand(id)
        return if (commandResult == null) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity.ok(commandResult)
        }
    }
}