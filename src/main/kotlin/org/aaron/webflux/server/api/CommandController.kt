package org.aaron.webflux.server.api

import mu.KLogging
import org.aaron.webflux.server.model.CommandAPIResult
import org.aaron.webflux.server.service.CommandService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/commands")
class CommandController(
        private val commandService: CommandService) {

    companion object : KLogging()

    @GetMapping("/{id}")
    fun runCommand(@PathVariable(required = true) id: String): Mono<ResponseEntity<CommandAPIResult>> {
        logger.debug { "runCommand id = ${id}" }
        return commandService.runCommand(id).map { commandAPIResult ->
            ResponseEntity.ok(commandAPIResult)
        }.switchIfEmpty(Mono.just(ResponseEntity(HttpStatus.NOT_FOUND)))
    }
}