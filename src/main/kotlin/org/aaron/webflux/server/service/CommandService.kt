package org.aaron.webflux.server.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.aaron.webflux.server.config.CommandConfig
import org.aaron.webflux.server.model.Command
import org.aaron.webflux.server.model.CommandAPIResult
import org.springframework.stereotype.Service
import java.io.InputStreamReader
import java.time.OffsetDateTime

private val logger = KotlinLogging.logger {}

@Service
class CommandService(
        commandConfig: CommandConfig) {

    private val idToCommand: Map<String, Command> = commandConfig.commands
            .map { it.id to it }
            .toMap()

    private val commandList = idToCommand.values.toList()

    fun getById(id: String): Command? {
        return idToCommand[id]
    }

    fun getCommands(): List<Command> {
        return commandList
    }

    suspend fun runCommand(id: String): CommandAPIResult? {
        val command = getById(id) ?: return null

        return withContext(Dispatchers.IO) {
            try {
                val commandAndArgsList = listOf(command.command) + command.arguments
                val processBuilder = ProcessBuilder(commandAndArgsList)
                processBuilder.redirectErrorStream(true)
                logger.debug { "start process $commandAndArgsList" }
                val process = processBuilder.start()
                val exitValue = process.waitFor()
                val output = InputStreamReader(process.inputStream)
                        .readLines()
                        .joinToString(separator = "\n")
                logger.debug { "exitValue = $exitValue" }
                CommandAPIResult(
                        command = command,
                        now = OffsetDateTime.now(),
                        output = output,
                        exitValue = exitValue)
            } catch (e: Exception) {
                logger.warn(e) { "runCommand $command" }
                CommandAPIResult(
                        command = command,
                        now = OffsetDateTime.now(),
                        output = "command error ${e.message}",
                        exitValue = -1)
            }
        }
    }
}