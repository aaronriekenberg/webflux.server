package org.aaron.webflux.server.service

import mu.KLogging
import org.aaron.webflux.server.config.CommandConfig
import org.aaron.webflux.server.model.Command
import org.aaron.webflux.server.model.CommandAPIResult
import org.aaron.webflux.server.model.MutableCommand
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import java.io.InputStreamReader
import java.time.OffsetDateTime

@Service
class CommandService(
        commandConfig: CommandConfig) {

    companion object : KLogging()

    private val idToCommand: Map<String, Command> = commandConfig.commands
            .asSequence()
            .map(MutableCommand::toCommand)
            .map { it.id to it }
            .toMap()

    private val runCommandScheduler: Scheduler =
            Schedulers.newElastic("runCommandScheduler", commandConfig.elasticThreadTTLSeconds)

    fun getById(id: String): Mono<Command> {
        return Mono.justOrEmpty(idToCommand[id])
    }

    fun getCommands(): Flux<Command> {
        return Flux.fromIterable(idToCommand.values)
    }

    fun runCommand(id: String): Mono<CommandAPIResult> {
        return getById(id).flatMap { command ->
            val blockingWrapper = Mono.fromCallable {
                try {
                    val commandAndArgs = listOf(command.command) + command.arguments
                    val processBuilder = ProcessBuilder(commandAndArgs)
                    processBuilder.redirectErrorStream(true)
                    logger.debug { "start process $commandAndArgs" }
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
            blockingWrapper.subscribeOn(runCommandScheduler)
        }
    }
}