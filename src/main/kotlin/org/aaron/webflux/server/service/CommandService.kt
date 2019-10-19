package org.aaron.webflux.server.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.aaron.webflux.server.config.CommandConfig
import org.aaron.webflux.server.model.Command
import org.aaron.webflux.server.model.CommandAPIResult
import org.aaron.webflux.server.model.MutableCommand
import org.springframework.stereotype.Service
import java.io.InputStreamReader
import java.time.OffsetDateTime
import java.util.concurrent.Executors

private val logger = KotlinLogging.logger {}

@Service
class CommandService(
        commandConfig: CommandConfig) {

    private val idToCommand: Map<String, Command> = commandConfig.commands
            .asSequence()
            .map(MutableCommand::toCommand)
            .map { it.id to it }
            .toMap()

    private val runCommandDispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()

    fun getById(id: String): Command? {
        return idToCommand[id]
    }

    fun getCommands(): Collection<Command> {
        return idToCommand.values
    }

    suspend fun runCommand(id: String): CommandAPIResult? {
        val command = getById(id) ?: return null

        logger.info("runCommand before coroutineScope")

        return withContext(Dispatchers.IO) {
            try {
                val commandAndArgs = listOf(command.command) + command.arguments
                val processBuilder = ProcessBuilder(commandAndArgs)
                processBuilder.redirectErrorStream(true)
                logger.info { "start process $commandAndArgs" }
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