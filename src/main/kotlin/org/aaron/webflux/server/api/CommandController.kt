package org.aaron.webflux.server.api

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.aaron.webflux.server.model.CommandAPIResult
import org.aaron.webflux.server.service.CommandService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/commands")
class CommandController(
        private val commandService: CommandService) {

    private val channel = Channel<String>(100)

    @PostConstruct
    fun postConstruct() {
        repeat(10) {
            GlobalScope.launch {
                runChannelConsumer(it, channel)
            }
        }
    }

    private suspend fun runChannelConsumer(id: Int, channel: ReceiveChannel<String>) {
        logger.info { "runChannelConsumer $id $channel" }
        while (true) {
            val s = channel.receive()
            logger.info { "consumer $id received '$s'" }
        }
    }

    @GetMapping("/{id}")
    suspend fun runCommand(@PathVariable(required = true) id: String): ResponseEntity<CommandAPIResult> {
        logger.debug { "runCommand id = $id" }

        val result = channel.offer("hello world")
        logger.info { "offer returned $result" }

        val commandResult = commandService.runCommand(id)
        return if (commandResult == null) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity.ok(commandResult)
        }
    }
}